//
//  MainMenuViewController.m
//  StepTracker
//
//  Created by Michael Hari on 4/4/13.
//  Copyright (c) 2013 San Jose State University. All rights reserved.
//

#import "MainMenuViewController.h"
#import "RecordViewController.h"
#import "WorkoutsViewController.h"

@interface MainMenuViewController ()

@end

@implementation MainMenuViewController
@synthesize loggedInUsername;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // self is not nil
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
     NSLog(@"LoggedInUserName: %@",self.loggedInUsername);
}

-(void) viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [[UIApplication sharedApplication] setIdleTimerDisabled: NO];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    if([[segue identifier] isEqualToString:@"recordSegue"]){
        RecordViewController *recordViewController = [segue destinationViewController];
        [recordViewController setLoggedInUsername:self.loggedInUsername];
    }else if([[segue identifier] isEqualToString:@"workoutSegue"]){
        WorkoutsViewController *workoutsViewController = [segue destinationViewController];
        [workoutsViewController setLoggedInUsername:self.loggedInUsername];
    }
}

@end
