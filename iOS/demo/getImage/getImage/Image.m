//
//  Image.m
//  getImage
//
//  Created by Athrun on 12-3-20.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//

#import "Image.h"
#import <QuartzCore/QuartzCore.h>

@implementation Image


+(void)getImage:(UIView *)v index:(NSInteger )elementName
{
    NSString *currentPath = [[NSBundle mainBundle] resourcePath];
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
        
        currentPath = [currentPath stringByAppendingFormat:@"/../Documents"];
        NSString *path = [currentPath stringByAppendingFormat:@"/%@.png",elementName];
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
            currentPath = [currentPath stringByAppendingFormat:@"/../Documents"];
            //NSLog(currentPath);
            NSString *path = [currentPath stringByAppendingFormat:@"/%s-%d.png",[imageHead UTF8String],index];
            NSLog(path);
            if ([UIImagePNGRepresentation(image) writeToFile:path atomically:YES])
            {
                index += 1;
                NSLog(path);
                NSLog(@"Succeeded!");
            }
            else 
            {
                NSLog(@"Failed!");
            }
        }
	}
}

@end
