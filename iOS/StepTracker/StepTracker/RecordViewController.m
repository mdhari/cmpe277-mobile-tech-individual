//
//  RecordViewController.m
//  StepTracker
//
//  Created by Michael Hari on 4/9/13.
//  Copyright (c) 2013 San Jose State University. All rights reserved.
//

#import "RecordViewController.h"
#import "ResultsViewController.h"
#import "WorkoutSummary.h"
#import "StepTrackerAppDelegate.h"
#import <CoreMotion/CoreMotion.h>

@interface RecordViewController ()

@end

@implementation RecordViewController
@synthesize loggedInUsername;

NSString *start = @"Start";
NSString *stop = @"Stop";
const float CALORIES_PER_STEP = 0.05f;
float threshold = 0.2f;
float lastReading = 0.0f;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

-(void) viewWillAppear:(BOOL)animated{
    [super viewWillAppear:animated];
    [[UIApplication sharedApplication] setIdleTimerDisabled: YES];
}

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view.
    isRunning=NO;
    NSLog(@"LoggedInUserName: %@",self.loggedInUsername);
//    CMMotionManager *motionManager = [[CMMotionManager alloc] init];
//    motionManager.deviceMotionUpdateInterval = 1.0/60.0;
//    
//    if (motionManager.deviceMotionAvailable ) {
//        NSLog(@"deviceMotionIsAvailable");
//        NSOperationQueue *queue = [NSOperationQueue currentQueue];
//        [motionManager startDeviceMotionUpdatesToQueue:queue
//                                           withHandler:^ (CMDeviceMotion *motionData, NSError *error) {
////                                               CMAttitude *attitude = motionData.attitude;
////                                               CMAcceleration gravity = motionData.gravity;
////                                               CMAcceleration userAcceleration = motionData.userAcceleration;
//                                               CMRotationRate rotate = motionData.rotationRate;
//                                               NSLog(@"x: %f y: %f z: %f",rotate.x,rotate.y,rotate.z);
////                                               CMCalibratedMagneticField field = motionData.magneticField;
//                                               float absVal = fabs(rotate.x);
//                                               if((fabs((absVal-lastReading)) > threshold)){
//                                                   lastReading = absVal;
//                                                   steps++;
//                                               }
//                                               [self updateMetrics];
//                                           }];
//        
//        
//    }
//    motionManager = [[CMMotionManager alloc] init];
//    motionManager.gyroUpdateInterval = 1.0/2.0; // Update every 1/2 second.
//    
//    if (motionManager.gyroAvailable) {
//        queue = [NSOperationQueue currentQueue];
//        [motionManager startGyroUpdatesToQueue:queue
//                                   withHandler: ^ (CMGyroData *gyroData, NSError *error) {
//                                       CMRotationRate rotate = gyroData.rotationRate;
//                                       NSLog(@"x: %f y: %f z: %f",rotate.x,rotate.y,rotate.z);
//                                       float absVal = fabs(rotate.x);
//                                       NSLog(@"x: %f absVal: %f lastReading: %f calc: %f threshold: %f",rotate.x,absVal,lastReading,(fabs((absVal-lastReading))), threshold);
//                                       if((fabs((absVal-lastReading)) > threshold)){
//                                          lastReading = absVal;
//                                          steps++;
//                                       }
//                                       [self updateMetrics];
//                                       
//                                   }];
//    }
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

-(BOOL) shouldAutorotateToInterfaceOrientation:(UIInterfaceOrientation)toInterfaceOrientation{
    return toInterfaceOrientation==UIInterfaceOrientationPortrait;
}

- (UIInterfaceOrientation)preferredInterfaceOrientationForPresentation{
    return UIInterfaceOrientationPortrait;
}
-(BOOL)shouldAutorotate{
    return YES;
}

-(NSUInteger)supportedInterfaceOrientations{
    return UIInterfaceOrientationMaskPortrait;
}

-(void)updateMetrics{
    calories = (int) (steps * CALORIES_PER_STEP);
    [self.stepCountLabel setText:[NSString stringWithFormat:@"%03d", steps]];
    [self.caloriesCountLabel setText:[NSString stringWithFormat:@"%03d", calories]];
    [self.timeIntervalLabel setText:[NSString stringWithFormat:@"%02d:%02d:%02d", hours,minutes,seconds]];
}

- (void)updateTimer{
    seconds++;
    if (seconds == 60) {
        seconds = 0;
        minutes++;
    }
    
    if (minutes == 60) {
        minutes = 0;
        hours++;
    }
    
    [self updateMetrics];
}

- (void)viewDidUnload {
    [self setTimeIntervalLabel:nil];
    [self setStartStopBtn:nil];
    [self setStepCountLabel:nil];
    [self setCaloriesCountLabel:nil];
    if(motionManager!=nil){
    [motionManager stopDeviceMotionUpdates];
        motionManager=nil;
    }
    [super viewDidUnload];
}
- (IBAction)startStopBtn:(id)sender {
    NSLog(@"startStopBtn clicked");
    if(isRunning){ // stop the timer
        isRunning=NO;
        [self.startStopBtn setTitle:start forState:UIControlStateNormal];
        [timeTimer invalidate];
        timeTimer = nil;
        [motionManager stopDeviceMotionUpdates];
        motionManager = nil;
    }else{ // start the timer
        isRunning=YES;
        [self.startStopBtn setTitle:stop forState:UIControlStateNormal];
        timeTimer = [NSTimer scheduledTimerWithTimeInterval:1.0 target:self selector:@selector(updateTimer) userInfo:nil repeats:YES];
        motionManager = [[CMMotionManager alloc] init];
        motionManager.gyroUpdateInterval = 1.0/2.0; // Update every 1/2 second.
        if (motionManager.gyroAvailable) {
            queue = [NSOperationQueue currentQueue];
            [motionManager startGyroUpdatesToQueue:queue
                                       withHandler: ^ (CMGyroData *gyroData, NSError *error) {
                                           CMRotationRate rotate = gyroData.rotationRate;
                                           NSLog(@"x: %f y: %f z: %f",rotate.x,rotate.y,rotate.z);
                                           float absVal = fabs(rotate.x);
                                           NSLog(@"x: %f absVal: %f lastReading: %f calc: %f threshold: %f",rotate.x,absVal,lastReading,(fabs((absVal-lastReading))), threshold);
                                           if((fabs((absVal-lastReading)) > threshold)){
                                               lastReading = absVal;
                                               steps++;
                                           }
                                           [self updateMetrics];
                                           
                                       }];
        }
    }
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    NSLog(@"prepareForSegue");
    if(isRunning){
        [self startStopBtn:self];
    }
    StepTrackerAppDelegate *appDelegate = (StepTrackerAppDelegate *)[[UIApplication sharedApplication] delegate];
    WorkoutSummary *workoutSummary = (WorkoutSummary *)[NSEntityDescription insertNewObjectForEntityForName:@"WorkoutSummary" inManagedObjectContext:appDelegate.managedObjectContext];
    [workoutSummary setSteps:[NSNumber numberWithInt:steps]];
    [workoutSummary setCalories:[NSNumber numberWithInt:calories]];
    [workoutSummary setTimeElapsed:[self.timeIntervalLabel text]];
    [workoutSummary setUsername:self.loggedInUsername];
    [workoutSummary setWorkout_date:[NSDate date]];
    NSError *error = nil;
    if (![appDelegate.managedObjectContext save:&error]) {
        // Handle the error.
    }
    
    ResultsViewController *resultsViewController = [segue destinationViewController];
    [resultsViewController setSteps:steps];
    [resultsViewController setCalories:calories];
    [resultsViewController setElaspedTime:[self.timeIntervalLabel text]];
    
}

@end
