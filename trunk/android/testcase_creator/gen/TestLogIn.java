package com.taobao.android.client.test;
import org.athrun.android.framework.AthrunTestCase;
import org.athrun.android.framework.viewelement.VieweElement;
import org.athrun.android.framework.viewelement.AbsListViewElement;
import org.athrun.android.framework.viewelement.CheckableElement;
import org.athrun.android.framework.viewelement.ScrollViewElement;
import org.athrun.android.framework.viewelement.SlideableElement;
import org.athrun.android.framework.viewelement.TextViewElement;
import org.athrun.android.framework.viewelement.ToastElement;
import org.athrun.android.framework.viewelement.ViewGroupElement;
import org.athrun.android.framework.viewelement.VieweElement;
import org.athrun.android.framework.AthrunDevice;
public class Test extends AthrunTestCase {
  public Test() throws Exception {
    super("com.taobao.taobao","com.taobao.taobao.MainActivity2");
  }
  public void testBug() throws Exception {
    //("{viewid=@adbanner_lay, actiontype=click, activity=com.taobao.tao.MainActivity2}");
    ViewElement modify_y22jm_this=findElementById("adbanner_lay",ViewElement.class);
    modify_y22jm_this.doClick();
    
    //("{viewid=@searchedit, actiontype=type, inputtext=男表 个性镂空时尚, activity=com.taobao.tao.TaoApplication}");
    TextViewElement modify_w536r_this=findElementById("searchedit",TextViewElement.class);
    modify_w536r_this.setText("男表 个性镂空时尚");
    
    //("{itemtype=LinearLayout, viewtext=Wilon/威龙正品男士手表 个, parenttype=android.widget.ListView, actiontype=itemclick, parentid=@searchgoodsList, activity=com.taobao.tao.SearchListActivity, viewpos=2}");
    AbsListViewElement modify_4slrd_this=findElementById("searchgoodsList",AbsListViewElement.class);
    ViewGroupElement modify_jrob0_this=modify_4slrd_this.getChildByIndex(2,ViewGroupElement.class);
    modify_jrob0_this.doClick();
    
    //("{keycode=KeyEvent.KEYCODE_MENU, actiontype=keypress}");
    Thread.sleep(IViewElement.ANR_TIME);
    getDevice().pressKeys(KeyEvent.KEYCODE_MENU);
    
    //("{viewtext=退出, viewid=@menu8, actiontype=click, activity=com.taobao.tao.TaoApplication}");
    TextViewElement modify_fe3kc_this=findElementById("menu8",TextViewElement.class);
    modify_fe3kc_this.doClick();
    
    //("{viewtext=确 定, viewid=@TBDialog_buttons_OK, actiontype=click, activity=com.taobao.tao.DetailActivity2}");
    TextViewElement modify_m9ysq_this=findElementById("TBDialog_buttons_OK",TextViewElement.class);
    modify_m9ysq_this.doClick();
    
  }
}
