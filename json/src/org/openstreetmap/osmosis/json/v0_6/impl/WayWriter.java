// This software is released into the Public Domain.  See copying.txt for details.
package org.openstreetmap.osmosis.json.v0_6.impl;

import java.io.Writer;
import java.util.Collection;
import java.util.List;

import org.openstreetmap.osmosis.core.domain.v0_6.OsmUser;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.core.domain.v0_6.Way;
import org.openstreetmap.osmosis.core.domain.v0_6.WayNode;
import org.openstreetmap.osmosis.json.common.ElementWriter;


/**
 * Renders a way as xml.
 *
 * @author Brett Henderson
 */
public class WayWriter extends ElementWriter {
    /**
     * Write the ordered list of node-references of a way.
     */
    private WayNodeWriter wayNodeWriter;
    /**
     * Write the tags of a way.
     */
    private TagWriter tagWriter;
	private boolean first = true;


	/**
	 * Creates a new instance.
	 * 
	 * @param elementName
	 *            The name of the element to be written.
	 * @param indentLevel
	 *            The indent level of the element.
	 */
	public WayWriter(int indentLevel) {
		super(indentLevel);
		
		tagWriter = new TagWriter(indentLevel + 1);
		wayNodeWriter = new WayNodeWriter(indentLevel + 1);
	}
	
	/**
	 * Writes the way.
	 * 
	 * @param way
	 *            The way to be processed.
	 */
	public void process(Way way) {
		OsmUser user;
		List<WayNode> wayNodes;
		Collection<Tag> tags;
		
		user = way.getUser();
		
		startObject(first);
		addAttribute("id", way.getId(), true);
		addAttribute("version", way.getVersion(), false);
		addAttribute("timestamp", way.getFormattedTimestamp(getTimestampFormat()), false);
		
		if (!user.equals(OsmUser.NONE)) {
			addAttribute("uid", user.getId(), false);
			addAttribute("user", user.getName(), false);
		}
		
		if (way.getChangesetId() != 0) {
			addAttribute("changeset", way.getChangesetId(), false);
		}
		
		wayNodes = way.getWayNodes();
		tags = way.getTags();
		
		if (wayNodes.size() > 0 || tags.size() > 0) {
			objectKey("nodes", false);
			startList();
			for (WayNode wayNode : wayNodes) {
				wayNodeWriter.processWayNode(wayNode);
			}
			wayNodeWriter.reset();
			endList();
			
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
		
		wayNodeWriter.setWriter(writer);
		tagWriter.setWriter(writer);
	}

	public void reset() {
		first = true;
	}
}
