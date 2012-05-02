//
//  runner.m
//  getImage
//
//  Created by Athrun on 12-3-20.
//  Copyright (c) 2012年 __MyCompanyName__. All rights reserved.
//

#import "runner.h"
#import "Image.h"
#import <unistd.h>

@implementation runner

-(id)init
{
    self = [super init];
    [self performSelector:@selector(runCommand) withObject:nil afterDelay:2.0];
    
    return self;
}


-(void)runCommand
{
    int i=0;
    while (i!=10)
    {
        [Image getImage:NULL index:i];
        i=i+1;
        sleep(5);
    }
    
}

@end
