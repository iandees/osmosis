// This software is released into the Public Domain.  See copying.txt for details.
package org.openstreetmap.osmosis.json.v0_6.impl;

import java.io.Writer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.Locale;

import org.openstreetmap.osmosis.core.domain.v0_6.Node;
import org.openstreetmap.osmosis.core.domain.v0_6.OsmUser;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.json.common.ElementWriter;


/**
 * Renders a node as xml.
 *
 * @author Brett Henderson
 */
public class NodeWriter extends ElementWriter {
    /**
     * Write the tags of a node.
     */
   private TagWriter tagWriter;
   private NumberFormat numberFormat;
   private boolean first = true;

	/**
	 * Creates a new instance.
	 * 
	 * @param elementName
	 *            The name of the element to be written.
	 * @param indentLevel
	 *            The indent level of the element.
	 */
	public NodeWriter(int indentLevel) {
		super(indentLevel);
		
		tagWriter = new TagWriter(indentLevel + 1);
		
		// Only write the first 7 decimal places.
		// Write in US locale so that a '.' is used as the decimal separator.
		numberFormat = new DecimalFormat(
			"0.#######;-0.#######",
			new DecimalFormatSymbols(Locale.US)
		);
	}
	
	
	/**
	 * Writes the node.
	 * 
	 * @param node
	 *            The node to be processed.
	 */
	public void process(Node node) {
		OsmUser user;
		Collection<Tag> tags;
		
		user = node.getUser();
		
		startObject(first);
		addAttribute("id", node.getId(), true);
		addAttribute("version", node.getVersion(), false);
		addAttribute("timestamp", node.getFormattedTimestamp(getTimestampFormat()), false);
		
		if (!user.equals(OsmUser.NONE)) {
			addAttribute("uid", user.getId(), false);
			addAttribute("user", user.getName(), false);
		}
		
		if (node.getChangesetId() != 0) {
			addAttribute("changeset", node.getChangesetId(), false);
		}
		
		addAttribute("lat", node.getLatitude(), false);
		addAttribute("lon", node.getLongitude(), false);
		
		tags = node.getTags();
		
		if (tags.size() > 0) {
			objectKey("tags", false);
			startObject(true);
			
			for (Tag tag : tags) {
				tagWriter.process(tag);
			}
			
			tagWriter.reset();
			endObject();
		}
		
		endObject();
		
		if(first) {
			first = false;
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setWriter(final Writer writer) {
		super.setWriter(writer);
		
		tagWriter.setWriter(writer);
	}


	public void reset() {
		first = true;
	}
}
