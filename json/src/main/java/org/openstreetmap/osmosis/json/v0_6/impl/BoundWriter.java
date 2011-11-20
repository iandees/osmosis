// This software is released into the Public Domain.  See copying.txt for details.
package org.openstreetmap.osmosis.json.v0_6.impl;

import org.openstreetmap.osmosis.core.domain.v0_6.Bound;
import org.openstreetmap.osmosis.json.common.ElementWriter;

/**
 * @author KNewman
 * 
 */
public class BoundWriter extends ElementWriter {

	/**
	 * Creates a new instance.
	 * 
	 * @param elementName
	 *            The name of the element to be written.
	 * @param indentLevel
	 *            The indent level of the element.
	 */
	public BoundWriter(int indentLevel) {
		super(indentLevel);
	}


	/**
	 * Writes the bound.
	 * 
	 * @param bound
	 *            The bound to be processed.
	 */
	public void process(Bound bound) {

		// Only add the Bound if the origin string isn't empty
		if (bound.getOrigin() != "") {
			objectKey("bounds", true);
			startObject(true);
			// Write with the US locale (to force . instead of , as the decimal separator)
			// Use only 5 decimal places (~1.2 meter resolution should be sufficient for Bound)
			addAttribute("minlat", bound.getBottom(), true);
			addAttribute("minlon", bound.getLeft(), false);
			addAttribute("maxlat", bound.getTop(), false);
			addAttribute("maxlon", bound.getRight(), false);
			endObject();
		}
	}
}
