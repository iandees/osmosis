// This software is released into the Public Domain.  See copying.txt for details.
package org.openstreetmap.osmosis.json.v0_6;

import java.io.File;

import org.openstreetmap.osmosis.core.pipeline.common.TaskConfiguration;
import org.openstreetmap.osmosis.core.pipeline.common.TaskManager;
import org.openstreetmap.osmosis.core.pipeline.v0_6.SinkManager;
import org.openstreetmap.osmosis.json.common.CompressionMethod;
import org.openstreetmap.osmosis.json.common.JsonTaskManagerFactory;


/**
 * The task manager factory for an xml writer.
 * 
 * @author Ian Dees
 */
public class JsonWriterFactory extends JsonTaskManagerFactory {
	private static final String ARG_FILE_NAME = "file";
	private static final String DEFAULT_FILE_NAME = "dump.json";
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected TaskManager createTaskManagerImpl(TaskConfiguration taskConfig) {
		String fileName;
		File file;
		JsonWriter task;
		CompressionMethod compressionMethod;
		
		// Get the task arguments.
		fileName = getStringArgument(
			taskConfig,
			ARG_FILE_NAME,
			getDefaultStringArgument(taskConfig, DEFAULT_FILE_NAME)
		);
		compressionMethod = getCompressionMethodArgument(taskConfig, fileName);
		
		// Create a file object from the file name provided.
		file = new File(fileName);
		
		// Build the task object.
		task = new JsonWriter(file, compressionMethod);
		
		return new SinkManager(taskConfig.getId(), task, taskConfig.getPipeArgs());
	}
}
