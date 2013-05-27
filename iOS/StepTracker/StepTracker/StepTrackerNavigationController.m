//
//  StepTrackerNavigationController.m
//  StepTracker
//
//  Created by Michael Hari on 4/16/13.
//  Copyright (c) 2013 San Jose State University. All rights reserved.
//

#import "StepTrackerNavigationController.h"

@interface StepTrackerNavigationController ()

@end

@implementation StepTrackerNavigationController

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (BOOL)shouldAutorotate
{
    return self.topViewController.shouldAutorotate;
}
- (NSUInteger)supportedInterfaceOrientations
{
    return self.topViewController.supportedInterfaceOrientations;
}

@end
