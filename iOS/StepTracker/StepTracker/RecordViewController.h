//
//  RecordViewController.h
//  StepTracker
//
//  Created by Michael Hari on 4/9/13.
//  Copyright (c) 2013 San Jose State University. All rights reserved.
//

#import <UIKit/UIKit.h>
#import <CoreMotion/CoreMotion.h>

@interface RecordViewController : UIViewController{
    int seconds;
    int minutes;
    int hours;
    int steps;
    int calories;
    NSTimer *timeTimer;
    BOOL isRunning;
    CMMotionManager *motionManager;
    NSOperationQueue *queue;
}
@property (strong, nonatomic) IBOutlet UILabel *stepCountLabel;
@property (strong, nonatomic) IBOutlet UILabel *caloriesCountLabel;
@property (strong, nonatomic) IBOutlet UILabel *timeIntervalLabel;
- (IBAction)startStopBtn:(id)sender;
@property (strong, nonatomic) IBOutlet UIButton *startStopBtn;
@property(strong,nonatomic) NSString *loggedInUsername;
@end
