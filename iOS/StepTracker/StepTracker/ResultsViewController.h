//
//  ResultsViewController.h
//  StepTracker
//
//  Created by Michael Hari on 4/11/13.
//  Copyright (c) 2013 San Jose State University. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface ResultsViewController : UIViewController

@property int steps;
@property int calories;
@property(nonatomic,retain) NSString *elaspedTime;

@property (strong, nonatomic) IBOutlet UILabel *stepsTaken;
@property (strong, nonatomic) IBOutlet UILabel *caloriesBurned;
@property (strong, nonatomic) IBOutlet UILabel *timeTaken;
- (IBAction)homeBtn:(id)sender;

@end
