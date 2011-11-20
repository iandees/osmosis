// This software is released into the Public Domain.  See copying.txt for details.
package org.openstreetmap.osmosis.json.v0_6.impl;

import org.openstreetmap.osmosis.core.domain.v0_6.RelationMember;
import org.openstreetmap.osmosis.json.common.ElementWriter;


/**
 * Renders a relation member as xml.
 * 
 * @author Brett Henderson
 */
public class RelationMemberWriter extends ElementWriter {
	
	private MemberTypeRenderer memberTypeRenderer;
	private boolean first = true;
	
	
	/**
	 * Creates a new instance.
	 * 
	 * @param elementName
	 *            The name of the element to be written.
	 * @param indentLevel
	 *            The indent level of the element.
	 */
	public RelationMemberWriter(int indentLevel) {
		super(indentLevel);
		
		memberTypeRenderer = new MemberTypeRenderer();
	}
	
	
	/**
	 * Writes the way node.
	 * 
	 * @param relationMember
	 *            The wayNode to be processed.
	 */
	public void processRelationMember(RelationMember relationMember) {
		startObject(first);
		addAttribute("type", memberTypeRenderer.render(relationMember.getMemberType()), true);
		addAttribute("ref", relationMember.getMemberId(), false);
		addAttribute("role", relationMember.getMemberRole(), false);
		endObject();
		
		if(first) {
			first = false;
		}
	}
	
	public void reset() {
		first = true;
	}
}
