//
//  TBOperator.m
//  AppFramework
//
//  Created by nan fei on 5/25/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//
//  add by nan fei(nanfei@taobao.com)
//

#import "TBOperator.h"
#import "TouchSynthesis.h"
#import "TBTestLog.h"


@implementation TBOperator

//点击操作
+(bool)simluteTouch:(UIView *)object
{
	//操作对象非空
	if (object == nil)
	{

		NSString *log = [[NSString alloc] initWithFormat:@"### Command 'Touch' requires 'object' parameter.\n"];
		[TBTestLog TBError:log];
		
		return 0;
	}
	
	UITouch *touch = [[UITouch alloc] initInView_TB:object];
	UIEvent *event = [[UIEvent alloc] initWithTouch_TB:touch];
	NSSet *touches = [[NSMutableSet alloc] initWithObjects:&touch count:1];	
		
	[touch.view touchesBegan:touches withEvent:event];
	
	[touch setPhase:UITouchPhaseEnded];
	
	[touch.view touchesEnded:touches withEvent:event];
	
	[event release];
	[touches release];
	[touch release];
	
	
	return 1;
		
}

//编辑控件输入操作
+(bool)Set:(UIView *)object string:(NSString *)string
{
	//操作对象非空
	if (object == nil)
	{
		NSString *log = [[NSString alloc] initWithFormat:@"### Command 'Set' requires 'object' parameter.\n"];
		[TBTestLog TBError:log];
		
		return 0;
	}
		
	//赋予text对象，设置文本值
	UITextView *tv = object;
	NSRange range;
	range.location = 0;
	range.length = 0;
	[tv becomeFirstResponder];
	[tv setText:string];
	   
    //[tv resignFirstResponder]; ??
	//操作键盘done
	UIWindow *window;
	UIWindow *mw = [[UIApplication sharedApplication] windows];
	UIWindow *tempw = NULL;
	NSInteger wn = 2;
	NSInteger loopWN = 1;
	for (tempw in mw) 
	{
		if (loopWN == wn) 
		{
			window = tempw;
			break;
		}
		loopWN = loopWN+1;
	}
	
	UIView *v1,*v2,*v3,*v4,*v5,*v6;
	v1 = [[window subviews] objectAtIndex:0];
	v2 = [[v1 subviews] objectAtIndex:0];
	v3 = [[v2 subviews] objectAtIndex:0];
	v4 = [[v3 subviews] objectAtIndex:0];
	v5 = [[v4 subviews] objectAtIndex:0];
    
    //区分字符和数字键盘
    if ([[v5 subviews] count] < 9)
    {
        v6 = [[v5 subviews] objectAtIndex:4];
        [self simluteTouch:v6];
    }
	
	//返回操作成功
	return 1;	
	
}

//滚动屏幕操作
+(bool)scrollTo:(UIView *)object Number:(NSNumber *)indexNumber direction:(NSString *)directString
{
	//操作对象非空
	if (object == nil)
	{
		NSString *log = [[NSString alloc] initWithFormat:@"### Command 'scrollToRow' requires 'object' parameter.\n"];
		[TBTestLog TBError:log];
		
		return 0;
	}
    
    if ([directString isEqualToString:@"row"])
    {
        NSInteger temp = 1;
        UITableView *tview = object;
        //CGRect sectionRect = [tview rectForSection:temp];
              
        UITableViewCell *cell =nil;
        NSInteger cellIndex = 0;
      
        for (UIView *bv in [tview subviews]) {
            if([bv isKindOfClass:[UITableViewCell class]]){
                cellIndex++;
                if(cellIndex == [indexNumber integerValue]){
                    cell = bv;
                    break;
                }
            }           
        }
       
        NSIndexPath *indexPath =[tview indexPathForCell:cell];

        CGRect sectionRect = [tview rectForRowAtIndexPath:indexPath];
        sectionRect.size.height = tview.frame.size.height;
        
        [tview scrollRectToVisible:sectionRect animated:YES];
    }
    else
    {
        UIScrollView *sview = object;
        CGRect sectionRect = [[[sview subviews] objectAtIndex:[indexNumber integerValue]] frame];
        [sview scrollRectToVisible:sectionRect animated:YES];
    }
    
	return 1;
}

//获取控件属性操作v
+(NSString *)getProperties:(UIView *)object propert:(NSString *)properties
{
    NSString *failString = @"get properties faild!\n";
    
	//操作对象非空
	if (object == nil)
	{
		NSString *log = [[NSString alloc] initWithFormat:@"### Command 'getProperties' requires 'object' parameter.\n"];
		[TBTestLog TBError:log];
		
		return failString;
	}			
	
	if ([object respondsToSelector:@selector(text)])
	{
		//属性获取成功
		NSString *log = [[NSString alloc] initWithFormat:@"=== get the properties: %s \n",properties];
		[TBTestLog TBDebug:log];
		
		NSString *actualProperties = (NSString *)[object performSelector:@selector(text)];
		return actualProperties;
	}
	else
	{
		//属性获取失败，返回获取失败字符串
		NSString *log = [[NSString alloc] initWithFormat:@"### object with tag: %s doesn't suport %s method\n",[object tag],properties];
		[TBTestLog TBError:log];
		
		return failString;
	}
	
	
}

//校验属性
+(bool)checkProperties:(UIView *)object propert:(NSString *)properties expect:(NSString *)expectString
{
	//操作对象非空
	if (object == nil)
	{
		NSString *log = [[NSString alloc] initWithFormat:@"### Command 'checkProperties' requires 'object' parameter.\n"];
		[TBTestLog TBError:log];
		
		return 0;
	}
	
	NSString *log = [[NSString alloc] initWithFormat:@"=== checkProperites for object with tag: %d with properties: %@\n",[object tag],properties];
	[TBTestLog TBDebug:log];
	
	//获取控件的实际属性值
	NSString *actualProperties = [self getProperties:object propert:properties];
	
	//判断控件属性是否获取成功
	if (actualProperties == @"get properties faild!\n")
	{
		NSString *log = [[NSString alloc] initWithFormat:@"###  get properties faild\n"];
		[TBTestLog TBError:log];
		
		return 0;
	}
	
	//属性校验，是否对等
	
	if (![expectString isEqualToString:actualProperties])
	{
		NSString *log = [[NSString alloc] initWithFormat:@"### check faild! \n expectString is: %s \n actualString is: %s \n",expectString,actualProperties];
		[TBTestLog TBError:log];
		
		return 0;
	}
	
	//属性校验一致
	log = [[NSString alloc] initWithFormat:@"=== check pass!\n expectString is: %@ \n actualString is: %@ \n",expectString,actualProperties];
	[TBTestLog TBDebug:log];
	
	return 1;
	
}

+(bool)Switch:(UIView *)object state:(NSString *)stateString
{
	//操作对象非空
	if (object == nil)
	{
		NSString *log = [[NSString alloc] initWithFormat:@"### Command 'Switch' requires 'object' parameter.\n"];
		[TBTestLog TBError:log];
		
		return 0;
	}	
	
	UISwitch *sv = object;
	
	if ([stateString isEqualToString:@"on"] || [stateString isEqualToString:@"off"]) 
	{
		if ([stateString isEqualToString:@"on"]) 
		{
			[sv setOn:YES];
		}
		else 
		{
			[sv setOn:NO];
		}

	}
	else 
	{
		NSString *log = [[NSString alloc] initWithFormat:@"### 'state' parameter is not 'on' or 'off'.\n"];
		[TBTestLog TBError:log];
		
		return 0;
	}

	return 1;
}

@end
