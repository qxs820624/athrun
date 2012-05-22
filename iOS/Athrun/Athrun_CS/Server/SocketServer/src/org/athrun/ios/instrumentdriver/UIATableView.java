/**
 * 
 */
package org.athrun.ios.instrumentdriver;

/**
 * @author ziyu.hch
 *
 */
public class UIATableView extends UIAElement {

	/**
	 * @param guid
	 */
	public UIATableView(String guid) {
		super(guid);
		// TODO Auto-generated constructor stub
	}

	public UIATableView() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public UIATableCell [] cells() throws Exception{
		
		return UIAElementHelp.cellsArray(this.guid + ".cells()");
	}

}
