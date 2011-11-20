// This software is released into the Public Domain.  See copying.txt for details.
package org.openstreetmap.osmosis.json;

import java.util.HashMap;
import java.util.Map;

import org.openstreetmap.osmosis.core.pipeline.common.TaskManagerFactory;
import org.openstreetmap.osmosis.core.plugin.PluginLoader;
import org.openstreetmap.osmosis.json.v0_6.JsonWriterFactory;


/**
 * The plugin loader for the JSON tasks.
 * 
 * @author Ian Dees
 */
public class JsonPluginLoader implements PluginLoader {

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, TaskManagerFactory> loadTaskFactories() {
		Map<String, TaskManagerFactory> factoryMap;
		
		factoryMap = new HashMap<String, TaskManagerFactory>();
		
		factoryMap.put("write-json", new JsonWriterFactory());
		factoryMap.put("wj", new JsonWriterFactory());
		
		return factoryMap;
	}
}
