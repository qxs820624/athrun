//
//  TBRunner.m
//  AppFramework
//
//  Created by nan fei on 6/1/11.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//
//  add by nan fei(nanfei@taobao.com) 
//  add by su ying(suying.wxl@taobao.com)
//  add by zhi han(xiaoyan.liu@alipay.com)
//

#ifdef SCRIPT_DRIVEN_TEST_MODE_ENABLED

#import "TBRunner.h"
#import "TBOperator.h"
#import "TBElement.h"
#import "TBTestLog.h"
#import "TBAssert.h"


const float SCRIPT_RUNNER_INTER_COMMAND_DELAY = 3.0;
const float MAX_WAIT_ATTEMPTS = 60;
const float WAIT_ATTEMPT_DELAY = 0.25;
const float BACKBUTTON_WAIT_DELAY = 0.75;

@implementation TBTestRunner

-(id)init
{
    
	self = [super init];
	if (self != nil)
	{
		
		NSData *scriptData = [NSData dataWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"ScriptIndex" ofType:@"plist"]];
        
        // add by zhihan(xiaoyan.liu@alipay.com)
		if (scriptData == nil) {
			// 如果ScriptIndex.plist脚本文件不存在
			NSString *strScriptError = [[NSString alloc] initWithFormat:@"ScriptIndex.plist is not exist!"];
			
			// 打印脚本错误日志
			[TBTestLog scriptErrorLog:@"ScriptIndex" error:strScriptError];
			
			// 退出程序
			exit(1);
		}
		// zhihan add end
        
		arrayScript = [[NSPropertyListSerialization propertyListFromData:scriptData 
														mutabilityOption:NSPropertyListMutableContainers 
																  format:nil 
														errorDescription:nil] retain];
		testcaseIndex = [arrayScript count];
		testcaseIndex = 0;
	    scriptName = [arrayScript objectAtIndex:testcaseIndex];
							  
    }
    
    //调用自身的测试脚本执行函数，在被测应用加载完成后2秒
    [self retain];
    [self performSelector:@selector(runScript) withObject:nil afterDelay:2.0];
	
	return self;
}


-(void)dealloc
{
	[arrayCommands release];
	[super dealloc];
}

-(void)runScript
{
    // add by zhihan(xiaoyan.liu@alipay.com)
    //初始化变量
    commandIndex = 0;
    // zhihan add end
    
    //测试对象初始时，读取测试用例脚本
    NSData *fileData = [NSData dataWithContentsOfFile:[[NSBundle mainBundle] pathForResource:scriptName ofType:@"plist"]];
    
    // add by zhihan(xiaoyan.liu@alipay.com)
    if (fileData == nil) 
    {
        // 如果脚本文件不存在
        NSString *strScriptError = [[NSString alloc] initWithFormat:@"%@.plis is not exist", scriptName];
        
        // 打印脚本错误日志
        [TBTestLog scriptErrorLog:@"ScriptIndex" error:strScriptError];
        
        // 读取下一个脚本文件
        testcaseIndex = testcaseIndex+1;
        scriptName = [arrayScript objectAtIndex:testcaseIndex];
        
        [self performSelector:@selector(runScript) withObject:nil afterDelay:2.0];        
    }
    // zhihan add end
    else
    {
        arrayCommands = [[NSPropertyListSerialization propertyListFromData:fileData 
                                                      mutabilityOption:NSPropertyListMutableContainers 
                                                                format:nil 
                                                      errorDescription:nil] retain];
        //调用自身的测试指令执行函数，在被测应用加载完成后2秒
        //[self retain];
        [self performSelector:@selector(runCommand) withObject:nil afterDelay:2.0];
    

        [TBTestLog TBLog:@"\n\n"];
        NSString *temp = [[NSString alloc] initWithFormat:@"Test Case %@ start run!\n",scriptName];
        [TBTestLog TBRun:temp];
    }
}

-(void)runCommand
{
	
	@try 
    {
        NSString *run = @"";
	    NSString *debug = @"";
	    NSString *warn = @"";
	    NSString *err = @"";
	
	    //读取一条测试指令
        NSDictionary *command = [arrayCommands objectAtIndex:0];
	    NSString *commandName = [command objectForKey:@"command"];
        
	    //读取操作对象信息
	    NSString *viewXpath;
	    if ([command objectForKey:@"element"])
	    {
            viewXpath = [command objectForKey:@"element"];
	    }
	    else 
	    {
            viewXpath = [command objectForKey:@"viewXPath"];
	    }
        
        // add by zhihan(xiaoyan.liu@alipay.com)
        //操作指令和操作对象不能为空
        if (commandName == nil)
        {
            // 如果指令中command不存在
            NSString *strScriptError = [[NSString alloc] initWithFormat:@"item%d'command is not exist, so can't do Operator", commandIndex];
            
            // 打印脚本错误日志
            [TBTestLog scriptErrorLog:scriptName error:strScriptError];        }
        else if (viewXpath == nil) 
        {
            // 如果指令中command不存在
            NSString *strScriptError = [[NSString alloc] initWithFormat:@"item%d'element or viewXPath is not exist,so can't find this control!", commandIndex];
			
            // 打印脚本错误日志
            [TBTestLog scriptErrorLog:scriptName error:strScriptError];
        }
        // zhihan add end
        else
        {
            //获取操作对象
            UIView *viewTarget;
	        NSString *pageName = nil;
	        NSString *elementName = nil;

            NSArray *tree = [viewXpath componentsSeparatedByString:@":"];
	        pageName = [tree objectAtIndex:0];
	        elementName = [tree objectAtIndex:1];
	        viewTarget = [[TBElement alloc] find_element:pageName elementName:elementName];
        
	        //截屏
	        [TBTestLog imageLog:NULL elementName:elementName];

	
	        //分流操作指令			
	        if ([commandName isEqualToString:@"simluteTouch"])
	        {
                if ([TBOperator simluteTouch:viewTarget])   //点击操作
		        {
                    run = @"";
			        run = [run stringByAppendingString:pageName];
			        run = [run stringByAppendingString:@"-"];
			        run = [run stringByAppendingString:elementName];
			        run = [run stringByAppendingString:@" touch is ok!\n"];
			        [TBTestLog TBRun:run];
		        }
		        else 
		        {
                    err = @"";
                    err = [err stringByAppendingString:pageName];
                    err = [err stringByAppendingString:@"-"];
                    err = [err stringByAppendingString:elementName];
                    err = [err stringByAppendingString:@" touch failed!\n"];
                    [TBTestLog TBError:err];
		        }

            }
            else if ([commandName isEqualToString:@"Set"])
            {
                NSString *valueString = [command objectForKey:@"valueString"];  //输入操作
                
                // add by zhihan(xiaoyan.liu@alipay.com)
			    if (valueString == nil) 
                {
                    // 如果指令中的操作现在还不支持
                    NSString *strScriptError = [[NSString alloc] initWithFormat:@"valueString isn't exist in item%d!", commandIndex];
				
				    // 打印脚本错误日志
				    [TBTestLog scriptErrorLog:scriptName error:strScriptError];
				
                }
	    	    // zhihan add end
            
		        //支持将输入值变量化 add by suying++++++++++++++++++++++++++++++++++++++++++++++++++++	
                NSRange cRange = [valueString rangeOfString:@"Consts"];
                if(cRange.length!=0)
                {
                    NSData *constsData = [NSData dataWithContentsOfFile:[[NSBundle mainBundle] pathForResource:@"Consts" ofType:@"plist"]];
                    NSArray *arrConsts = [[NSPropertyListSerialization propertyListFromData:constsData 
			                                                           mutabilityOption:NSPropertyListMutableContainers 
																				 format:nil 
			                                                           errorDescription:nil] retain];
            
                    NSArray *valueArray = [valueString componentsSeparatedByString:@":"];		
                    NSString *varName = [valueArray objectAtIndex:1];
		            if ([arrConsts objectForKey:varName])
				    {
                        valueString = [arrConsts objectForKey:varName];
				    }
				    else 
				    {
                        valueString = @"";
					    err = @"";
					    err = [err stringByAppendingString:varName];
					    err = [err stringByAppendingString:@" variable not found! value was set NULL!\n"];
					    [TBTestLog TBError:err];
				    }
                }
                //支持将输入值变量化 add by suying++++++++++++++++++++++++++++++++++++++++++++++++++++	
			
			    if ([TBOperator Set:viewTarget string:valueString])
			    {
                    run = @"";
                    run = [run stringByAppendingString:pageName];
                    run = [run stringByAppendingString:@"-"];
                    run = [run stringByAppendingString:elementName];
                    run = [run stringByAppendingString:@" set is ok!\n"];
				    [TBTestLog TBRun:run];
			    }
			    else 
			    {
                    err = @"";
                    err = [err stringByAppendingString:pageName];
				    err = [err stringByAppendingString:@"-"];
				    err = [err stringByAppendingString:elementName];
				    err = [err stringByAppendingString:@" set failed!\n"];
				    [TBTestLog TBError:err];
                }
            }
	        else if ([commandName isEqualToString:@"scrollToRow"])
            {
                NSNumber *rectNumber = NULL;
                NSString *directString = NULL;
                if ([command objectForKey:@"rowIndex"]) 
                {
                    rectNumber = [command objectForKey:@"rowIndex"];
                    directString = @"row";
                }
                else if ([command objectForKey:@"columnIndex"])
                {
                    rectNumber = [command objectForKey:@"columnIndex"];
                    directString = @"col";
                }
                else
                {
                    err = @"";
                    err = [err stringByAppendingString:@"scrollToRow need argument with rowIndex or columnIndex!\n"];;
				    [TBTestLog TBError:err];
                }
            
                if ([TBOperator scrollTo:viewTarget Number:rectNumber direction:directString]) 
                {
                    run = @"";
				    run = [run stringByAppendingString:pageName];
				    run = [run stringByAppendingString:@"-"];
				    run = [run stringByAppendingString:elementName];
                    run = [run stringByAppendingString:@" scroll is ok!\n"];
				    [TBTestLog TBRun:run];
                }
                else
                {
                    err = @"";
				    err = [err stringByAppendingString:pageName];
				    err = [err stringByAppendingString:@"-"];
				    err = [err stringByAppendingString:elementName];
				    err = [err stringByAppendingString:@" scroll failed!\n"];
				    [TBTestLog TBError:err];
                }
            }
	        else if ([commandName isEqualToString:@"getProperties"])
		    {
                NSString *Gpropert = [command objectForKey:@"properties"];   //获取控件属性
			    if ([TBOperator getProperties:viewTarget propert:Gpropert])
			    {
                    run = @"";
                    run = [run stringByAppendingString:pageName];
                    run = [run stringByAppendingString:@"-"];
                    run = [run stringByAppendingString:elementName];
                    run = [run stringByAppendingString:@" get properties is ok!\n"];
				    [TBTestLog TBRun:run];
                }
			    else 
                {
                    err = @"";
                    err = [err stringByAppendingString:pageName];
                    err = [err stringByAppendingString:@"-"];
                    err = [err stringByAppendingString:elementName];
                    err = [err stringByAppendingString:@" get properties failed!\n"];
				    [TBTestLog TBError:err];
                }
            }
            else if ([commandName isEqualToString:@"checkProperties"])
            {
                NSString *Cpropert = [command objectForKey:@"properties"];   //进行属性对比
			    NSString *CexpectString = [command objectForKey:@"expectString"];
			    if ([TBOperator checkProperties:viewTarget propert:Cpropert expect:CexpectString])
			    {
                    run = @"";
                    run = [run stringByAppendingString:pageName];
                    run = [run stringByAppendingString:@"-"];
                    run = [run stringByAppendingString:elementName];
                    run = [run stringByAppendingString:@" check properties is ok!"];
				    [TBTestLog TBRun:run];
                }
			    else 
                {
                    err = @"";
                    err = [err stringByAppendingString:pageName];
                    err = [err stringByAppendingString:@"-"];
                    err = [err stringByAppendingString:elementName];
                    err = [err stringByAppendingString:@" check properties failed!"];
                    [TBTestLog TBError:err];
                }
            }
	        else if([commandName isEqualToString:@"sleep"])
            {
                NSString *numberStr=[command objectForKey:@"number"];
		        int number=[numberStr intValue];
		        sleep(number);
            }
	
	        else if([commandName isEqualToString:@"Switch"])
            {
                NSString *stateString = [command objectForKey:@"state"];
		        if ([TBOperator Switch:viewTarget state:stateString])
                {
                    run = [run stringByAppendingString:pageName];
			        run = [run stringByAppendingString:@"-"];
			        run = [run stringByAppendingString:elementName];
			        run = [run stringByAppendingString:@" Switch is ok!\n"];
			        [TBTestLog TBRun:run];
                }
		        else 
                {
                    err = [err stringByAppendingString:pageName];
			        err = [err stringByAppendingString:@"-"];
			        err = [err stringByAppendingString:elementName];
			        err = [err stringByAppendingString:@" Switch failed!\n"];
			        [TBTestLog TBError:err];
                }
            }
	
	/*suying add-------
	 
	 else if([commandName isEqualToString:@"assertEquals"]){
	 
	 
	 NSString *expected = [command objectForKey:@"expectedString"];
	 NSString *received = [command objectForKey:@"receivedString"];
	 NSString *message=[command objectForKey:@"messageString"];  
	 
	 [TBAssert assertEquals:expected receivedStr: received messageStr: message];    
	 
	 } 
	 else if([commandName isEqualToString:@"assertTrue"]){
	 
	 //   +(void)assertTrue:(BOOL)expression messageStr: (NSString *)message;
	 //         +(void)assertFalse:(BOOL)expression messageStr: (NSString *)message;
	 //         +(void)assertNotNull:(UIView *)object messageStr: (NSString *)message 
	 //         +(void)assertContainText:(NSString *) valuePage  valueVerify: (NSString *) messageStr: (NSString *)message;         
	 //         
	 
	 NSString *expressionStr = [command objectForKey:@"expression"];
	 BOOL expression=[expressionStr boolValue];
	 NSString *message=[command objectForKey:@"messageString"];  
	 
	 [TBAssert assertTrue:expression messageStr:message]; 
	 
	 } 
	 else if([commandName isEqualToString:@"assertFalse"]){
	 
	 
	 NSString *expressionStr = [command objectForKey:@"expression"];
	 BOOL expression=[expressionStr boolValue];
	 NSString *message=[command objectForKey:@"messageString"];  
	 
	 [TBAssert assertFalse:expression messageStr:message]; 
	 }  
	 else if([commandName isEqualToString:@"assertNotNull"]){
	 
	 
	 //此操作必须和viewXPath指令一起使用
	 NSString *message=[command objectForKey:@"messageString"];  
	 
	 [TBAssert assertNotNull:viewTarget messageStr:message]; 
	 }*/
	 
            else if([commandName isEqualToString:@"assertContainText"])
            {
                //此操作必须和viewXPath指令一起使用
	            NSString *elementText=[TBOperator getProperties:viewTarget propert:@"text"];	 	 		 
	            NSString *verifyText=[command objectForKey:@"valueVerify"];
	            NSString *message=[command objectForKey:@"messageString"];  
	 
	            if ([TBAssert assertContainText:elementText valueVerify:verifyText messageStr:message]) 
                {
                    run = @"";
		            run = [run stringByAppendingString:@"TestCase:"];
		            run = [run stringByAppendingString:scriptName];
		            run = [run stringByAppendingString:@"==="];
		 
		            run = [run stringByAppendingString:message];
		            [TBTestLog TBRun:run];
                }
	            else 
	            {
                    err = @"";
		            err = [run stringByAppendingString:@"TestCase:"];
                    err = [run stringByAppendingString:scriptName];
		            err = [run stringByAppendingString:@"==="];
                    err = [err stringByAppendingString:@" assertContainText failed!"];
		            [TBTestLog TBError:err];
                }
            }  
            else
		    {
                //操作指令未匹配到，系统暂时不支持
                err = [[NSString alloc] initWithFormat: @"the command:%s does't support\n",[commandName UTF8String]];
			    [TBTestLog TBError:err];
            
                // add by zhihan(xiaoyan.liu@alipay.com)
				// 如果指令中的操作现在还不支持
				NSString *strScriptError = [[NSString alloc] initWithFormat:@"item%d'command %@ does't support!", commandIndex, commandName];
				
				// 打印脚本错误日志
				[TBTestLog scriptErrorLog:scriptName error:strScriptError];
                
                exit(1);				
			}
            // zhihan add end

        }	
    }
	@catch (NSException * e) 
	{
		NSString *eString = [NSString stringWithFormat:@"exception %@:%@.\n",[e name],[e reason]];
		[TBTestLog TBRun:eString];
	}
	
	@finally 
	{
        //获取页面模型的pageName
        NSDictionary *commandDebug = [arrayCommands objectAtIndex:0];
        NSString *viewXpathDebug;
        if ([commandDebug objectForKey:@"element"])
        {
            viewXpathDebug = [commandDebug objectForKey:@"element"];
        }
        else 
        {
            viewXpathDebug = [commandDebug objectForKey:@"viewXPath"];
        }
        NSString *pageName = nil;
        NSArray *tree = [viewXpathDebug componentsSeparatedByString:@":"];
        pageName = [tree objectAtIndex:0];
        
        NSString *strDate= [TBTestLog dateString];
        NSString *overString = [[NSString alloc] initWithFormat:@"\n\n %@ this page:%@ is over! \n\n",strDate,pageName];
        [TBTestLog debugLog:overString];
        [overString release];
        
        //这条指令执行完成，从指令集中去除
        [arrayCommands removeObjectAtIndex:0];
        
        // add by zhihan(xiaoyan.liu@alipay.com)
		commandIndex++;  // 脚本指令序号加1
        // zhihan add end        
	 
	    if ([arrayCommands count] == 0)
	    {
            if (testcaseIndex == ([arrayScript count]-1))
	        {
                //指令集中指令执行完后，释放测试对象
	            [self performSelector:@selector(release) withObject:nil afterDelay:5.0];
	            //[self release];
	            //exit(0);
            }
	        else 
	        {
                NSString *tempEnd = [[NSString alloc] initWithFormat:@"Test Case %@ run end!\n",scriptName];
	            [TBTestLog TBRun:tempEnd];
	 
	            testcaseIndex = testcaseIndex+1;
	            scriptName = [arrayScript objectAtIndex:testcaseIndex];
                
                [self performSelector:@selector(runScript) withObject:nil afterDelay:3.0];
                
            }
        }
        else
        {
            //该指令执行完后，延时一段时间执行下一条指令
	        [self performSelector:@selector(runCommand) withObject:nil afterDelay:SCRIPT_RUNNER_INTER_COMMAND_DELAY];
        }
    }	
}


@end

#endif
