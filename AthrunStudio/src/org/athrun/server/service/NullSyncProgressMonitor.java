/**
 * 
 */
package org.athrun.server.service;

import org.athrun.ddmlib.SyncService.ISyncProgressMonitor;

/**
 * @author taichan
 *
 */
public class NullSyncProgressMonitor implements ISyncProgressMonitor {

	/* (non-Javadoc)
	 * @see org.athrun.ddmlib.SyncService.ISyncProgressMonitor#start(int)
	 */
	@Override
	public void start(int totalWork) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.athrun.ddmlib.SyncService.ISyncProgressMonitor#stop()
	 */
	@Override
	public void stop() {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.athrun.ddmlib.SyncService.ISyncProgressMonitor#isCanceled()
	 */
	@Override
	public boolean isCanceled() {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see org.athrun.ddmlib.SyncService.ISyncProgressMonitor#startSubTask(java.lang.String)
	 */
	@Override
	public void startSubTask(String name) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.athrun.ddmlib.SyncService.ISyncProgressMonitor#advance(int)
	 */
	@Override
	public void advance(int work) {
		// TODO Auto-generated method stub

	}

}
