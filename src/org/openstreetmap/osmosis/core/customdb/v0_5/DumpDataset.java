// This software is released into the Public Domain.  See copying.txt for details.
package org.openstreetmap.osmosis.core.customdb.v0_5;

import org.openstreetmap.osmosis.core.OsmosisRuntimeException;
import org.openstreetmap.osmosis.core.container.v0_5.Dataset;
import org.openstreetmap.osmosis.core.container.v0_5.DatasetReader;
import org.openstreetmap.osmosis.core.container.v0_5.EntityContainer;
import org.openstreetmap.osmosis.core.lifecycle.ReleasableIterator;
import org.openstreetmap.osmosis.core.task.v0_5.DatasetSinkSource;
import org.openstreetmap.osmosis.core.task.v0_5.Sink;


/**
 * Reads all data from a dataset.
 * 
 * @author Brett Henderson
 */
public class DumpDataset implements DatasetSinkSource {
	private Sink sink;
	private DatasetReader datasetReader;
	
	
	/**
	 * {@inheritDoc}
	 */
	
	public void setSink(Sink sink) {
		this.sink = sink;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	
	public void process(Dataset dataset) {
		ReleasableIterator<EntityContainer> bboxData;
		
		if (datasetReader != null) {
			throw new OsmosisRuntimeException("process may only be invoked once.");
		}
		
		datasetReader = dataset.createReader();
		
		// Pass all data within the dataset to the sink.
		bboxData = datasetReader.iterate();
		try {
			while (bboxData.hasNext()) {
				sink.process(bboxData.next());
			}
			
			sink.complete();
			
		} finally {
			bboxData.release();
		}
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	
	public void release() {
		if (datasetReader != null) {
			datasetReader.release();
		}
	}
}
