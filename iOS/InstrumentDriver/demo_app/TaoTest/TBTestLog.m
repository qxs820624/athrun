//
//  TBTestLog.m
//  AppFramework
//
//  Created by jerryding on 11-7-7.
//  Copyright 2011 __MyCompanyName__. All rights reserved.
//
//  add by nan fei(nanfei@taobao.com)
//  add by zhi han(xiaoyan.liu@alipay.com)
//

#import "TBTestLog.h"
#import "TBRunner.h"
#import <UIKit/UIKit.h>


@implementation TBTestLog

//输出page的view层结构
+(void)debugLog:(NSString *)str
{
	NSString *path = @"/AppFrameworkLog/AppFrameworkTree.log";
	NSFileHandle *file;
	file = [NSFileHandle fileHandleForWritingAtPath:path];
	if (file == nil)
    {
		NSData *startStr = [@"taobao iphone test is run!\n \n" dataUsingEncoding:NSUTF8StringEncoding];
		[startStr writeToFile:path atomically:YES];
		file = [NSFileHandle fileHandleForWritingAtPath:path];
	}
	[file seekToEndOfFile];
	NSData *dataToWrite = [str dataUsingEncoding:NSUTF8StringEncoding];
	[file writeData:dataToWrite];
}

//输出运行时抓取的图片
+(void)imageLog:(UIView *)v elementName:(NSString *)elementName
{
	if(v != NULL)
	{
        if (UIGraphicsBeginImageContextWithOptions != NULL)
        {
            UIGraphicsBeginImageContextWithOptions(v.frame.size, NO, 0.0);
        }
        else 
        {
            UIGraphicsBeginImageContext(v.frame.size);
        }
        [v.layer renderInContext:UIGraphicsGetCurrentContext()];
        UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
        UIGraphicsEndImageContext();
        NSString *path = [@"/AppFrameworkLog/image" stringByAppendingFormat:@"/%s.png",[elementName UTF8String]];
        if ([UIImagePNGRepresentation(image) writeToFile:path atomically:YES])
        {
            NSLog(@"Succeeded!");
        }
        else 
        {
            NSLog(@"Failed!");
        }
	}
	else 
	{
        UIWindow *w = [[UIApplication sharedApplication] windows];
        UIWindow *subW;
        NSString *imageHead = [NSString stringWithFormat:@"%@",[NSDate date]];
        NSInteger index = 1;
        
        for (subW in w) 
        {
            if (UIGraphicsBeginImageContextWithOptions != NULL)
            {
                UIGraphicsBeginImageContextWithOptions(subW.frame.size, NO, 0.0);
            }
            else 
            {		
                UIGraphicsBeginImageContext(subW.frame.size);
            }
            
            [subW.layer renderInContext:UIGraphicsGetCurrentContext()];
            UIImage *image = UIGraphicsGetImageFromCurrentImageContext();
            UIGraphicsEndImageContext();
            
            NSString *path = [@"/AppFrameworkLog/image" stringByAppendingFormat:@"/%s-%d.png",[imageHead UTF8String],index];
            if ([UIImagePNGRepresentation(image) writeToFile:path atomically:YES])
            {
                index += 1;
                NSLog(@"Succeeded!");
            }
            else 
            {
                NSLog(@"Failed!");
            }
        }
	}
}

//输出运行日志
+(void)TBLog:(NSString *)str 
{
	NSString *path = @"/AppFrameworkLog/AppFrameworkRun.log";
	NSFileHandle *file;
	file = [NSFileHandle fileHandleForWritingAtPath:path];
	if (file == nil)
    {
		NSData *startStr = [@"taobao iphone test is run!\n \n" dataUsingEncoding:NSUTF8StringEncoding];
		[startStr writeToFile:path atomically:YES];
		file = [NSFileHandle fileHandleForWritingAtPath:path];
	}
	[file seekToEndOfFile];
	NSData *dataToWrite = [str dataUsingEncoding:NSUTF8StringEncoding];
	[file writeData:dataToWrite];
}

+(void)TBDebug:(NSString *)str
{
	NSString *debugStr = [NSString stringWithFormat: @"%@---[Debug]:",[NSDate date]];
	debugStr = [debugStr stringByAppendingString:str];
	[self TBLog:debugStr];
}

+(void)TBWarn:(NSString *)str
{
	NSString *warnStr = [NSString stringWithFormat: @"%@---[Warn]:",[NSDate date]];
	warnStr = [warnStr stringByAppendingString:str];
	[self TBLog:warnStr];
}

+(void)TBError:(NSString *)str
{
	NSString *errorStr = [NSString stringWithFormat: @"%@---[Error]:",[NSDate date]];
	errorStr = [errorStr stringByAppendingString:str];
	[self TBLog:errorStr];
}

+(void)TBRun:(NSString *)str
{
	NSString *runStr = [NSString stringWithFormat: @"%@---[Run]:",[NSDate date]];
	runStr = [runStr stringByAppendingString:str];
	[self TBLog:runStr];
}

+(void)elementInfo:(UIView *)v pathNumber:(NSInteger )pathNumber pathString:(NSString *)path
{
	NSString *headS = [[NSString alloc] initWithFormat:@"--"];
	NSInteger i;
	for (i=0; i<pathNumber; i++) 
	{
		[TBTestLog debugLog:headS];
	}
	
	NSString *elementInfo = [v description];
	NSArray *infoArray = [elementInfo componentsSeparatedByString:@";"];
	NSString *stringInfo = @"";
	stringInfo = [stringInfo stringByAppendingString:[infoArray objectAtIndex:0]];
	stringInfo = [stringInfo stringByAppendingString:@";>>"];
	NSInteger j=1;
	for (j; j<=[infoArray count]-1; j++) 
	{
		NSRange match1 = [[infoArray objectAtIndex:j] rangeOfString:@"frame"];
		if (match1.location != NSNotFound)
		{
			stringInfo = [stringInfo stringByAppendingString:[infoArray objectAtIndex:j]];
			j = j+1;
			stringInfo = [stringInfo stringByAppendingString:[infoArray objectAtIndex:j]];
			continue;
		}
		
	}
	stringInfo = [stringInfo stringByAppendingString:@";>>"];
	NSString *temp = [[NSString alloc] initWithFormat:@"subviews (%d):%s",pathNumber,[stringInfo cStringUsingEncoding:NSUTF8StringEncoding]];
	[TBTestLog debugLog:temp];
	[temp release];
	
	
	if ([v respondsToSelector:@selector(text)])
	{
		NSString *l = [v text];
		[TBTestLog debugLog:@"text:"];
		[TBTestLog debugLog:l];	
		[TBTestLog debugLog:@";"];
	}
	
	if ([v respondsToSelector:@selector(tag)])
	{
		NSString *t = [NSString stringWithFormat:@">>Tag:%d;",[v tag]]; 
		[TBTestLog debugLog:t];
	}
    
    [TBTestLog debugLog:@" path:"];
    [TBTestLog debugLog:path];
    
	[TBTestLog debugLog:@"\n"];
}

// add by zhihan
// 脚本错误日志
+(void)scriptErrorLog:(NSString *)scriptName error:(NSString *)strError
{
	// 在指定目录下打开脚本错误日志文件
	NSString *path = @"/AppFrameworkLog/AppFrameworkScriptError.log"; // 文件路径
	NSFileHandle *file; 
	file = [NSFileHandle fileHandleForWritingAtPath:path];// 打开文件
	if (file == nil)
    {
		// 如果文件不存在 创建脚本错误日志文件
		NSData *startStr = [@"Script has error!\n \n" dataUsingEncoding:NSUTF8StringEncoding];
		[startStr writeToFile:path atomically:YES];
		file = [NSFileHandle fileHandleForWritingAtPath:path]; //  打开创建的问jain
	}
	
	[file seekToEndOfFile]; // 将文件内容指针移到文件尾部
	
	// 格式化写入的内容
	// 获取当前系统时间
	NSDate* date = [NSDate date];
	NSDateFormatter* formatter = [[[NSDateFormatter alloc] init] autorelease];
	[formatter setDateFormat:@"yyyy-MM-dd HH:mm:ss"];
	NSString* strDate = [formatter stringFromDate:date];
	
	NSString *str = [[NSString alloc] initWithFormat:@"%@[error]%@.plis:  %@\n\n", strDate, scriptName, strError]; 
	NSData *dataToWrite = [str dataUsingEncoding:NSUTF8StringEncoding]; // 将NString类型转换成NSData类型
	[file writeData:dataToWrite]; // 将日志内容写入文件
	
}
// zhihan add end

+(NSString *)dateString
{
    NSDate *date = [NSDate date];
    NSDateFormatter *formatter = [[[NSDateFormatter alloc] init] autorelease];
    [formatter setDateFormat:@"yyyy-mm-dd hh:mm:ss"];
    NSString *strDate = [formatter stringFromDate:date];
    return strDate;
}

@end
