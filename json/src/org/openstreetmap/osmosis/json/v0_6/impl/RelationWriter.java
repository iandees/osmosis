// This software is released into the Public Domain.  See copying.txt for details.
package org.openstreetmap.osmosis.json.v0_6.impl;

import java.io.Writer;
import java.util.Collection;
import java.util.List;

import org.openstreetmap.osmosis.core.domain.v0_6.OsmUser;
import org.openstreetmap.osmosis.core.domain.v0_6.Relation;
import org.openstreetmap.osmosis.core.domain.v0_6.RelationMember;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import org.openstreetmap.osmosis.json.common.ElementWriter;


/**
 * Renders a relation as xml.
 *
 * @author Brett Henderson
 */
public class RelationWriter extends ElementWriter {
    /**
     * Write the ordered list of members of a relation.
     */
    private RelationMemberWriter relationMemberWriter;
    /**
     * Write the tags of a relation.
     */
    private TagWriter tagWriter;
	private boolean first;


	/**
	 * Creates a new instance.
	 * 
	 * @param elementName
	 *            The name of the element to be written.
	 * @param indentLevel
	 *            The indent level of the element.
	 */
	public RelationWriter(int indentLevel) {
		super(indentLevel);
		
		tagWriter = new TagWriter(indentLevel + 1);
		relationMemberWriter = new RelationMemberWriter(indentLevel + 1);
	}
	
	
	/**
	 * Writes the relation.
	 * 
	 * @param relation
	 *            The relation to be processed.
	 */
	public void process(Relation relation) {
		OsmUser user;
		List<RelationMember> relationMembers;
		Collection<Tag> tags;
		
		user = relation.getUser();
		
		startObject(first);
		addAttribute("id", Long.toString(relation.getId()), true);
		addAttribute("version", Integer.toString(relation.getVersion()), false);
		addAttribute("timestamp", relation.getFormattedTimestamp(getTimestampFormat()), false);
		
		if (!user.equals(OsmUser.NONE)) {
			addAttribute("uid", Integer.toString(user.getId()), false);
			addAttribute("user", user.getName(), false);
		}
		
		if (relation.getChangesetId() != 0) {
			addAttribute("changeset", Long.toString(relation.getChangesetId()), false);
		}
		
		relationMembers = relation.getMembers();
		tags = relation.getTags();
		
		if (relationMembers.size() > 0 || tags.size() > 0) {

			objectKey("members", true);
			startList();
			for (RelationMember relationMember : relationMembers) {
				relationMemberWriter.processRelationMember(relationMember);
			}
			endList();
			
			objectKey("tags", false);
			startObject(false);
			for (Tag tag : tags) {
				tagWriter.process(tag);
			}
			tagWriter.reset();
			endObject();
			
		}

		endObject();
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setWriter(final Writer writer) {
		super.setWriter(writer);
		
		relationMemberWriter.setWriter(writer);
		tagWriter.setWriter(writer);
	}


	public void reset() {
		first = true;
	}
}
