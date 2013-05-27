//
//  ResultsViewController.m
//  StepTracker
//
//  Created by Michael Hari on 4/11/13.
//  Copyright (c) 2013 San Jose State University. All rights reserved.
//

#import "ResultsViewController.h"
#import "MainMenuViewController.h"

@interface ResultsViewController ()

@end

@implementation ResultsViewController
@synthesize steps,calories,elaspedTime;

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
    [self.stepsTaken setText:[NSString stringWithFormat:@"%03d", steps]];
    [self.caloriesBurned setText:[NSString stringWithFormat:@"%03d", calories]];
    [self.timeTaken setText:elaspedTime];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(void) viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [[UIApplication sharedApplication] setIdleTimerDisabled: NO];
}

- (void)viewDidUnload {
    [self setStepsTaken:nil];
    [self setCaloriesBurned:nil];
    [self setTimeTaken:nil];
    [super viewDidUnload];
}
- (IBAction)homeBtn:(id)sender {
    NSMutableArray *viewControllers = [NSMutableArray arrayWithArray:[[self navigationController] viewControllers]];
    [self.navigationController popToViewController:[viewControllers objectAtIndex:1] animated:YES];
}
@end
