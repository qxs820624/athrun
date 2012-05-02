//
//  ButtonClickViewController.h
//  SelfTesting
//
//  Created by Matt Gallagher on 22/11/08.
//  Copyright 2008 Matt Gallagher. All rights reserved.
//
//  Permission is given to use this source code file, free of charge, in any
//  project, commercial or otherwise, entirely at your risk, with the condition
//  that any redistribution (in part or whole) of source code must retain
//  this copyright and permission notice. Attribution in compiled projects is
//  appreciated but not required.
//

#import "PageViewController.h"

@interface ButtonClickViewController : PageViewController
{
	IBOutlet UILabel *label;
}

- (IBAction)buttonClicked:(id)sender;

@end
