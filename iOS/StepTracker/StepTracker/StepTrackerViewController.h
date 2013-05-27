//
//  StepTrackerViewController.h
//  StepTracker
//
//  Created by Michael Hari on 4/4/13.
//  Copyright (c) 2013 San Jose State University. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface StepTrackerViewController : UIViewController
@property (strong, nonatomic) IBOutlet UITextField *username;
@property (strong, nonatomic) IBOutlet UITextField *password;
- (IBAction)loginBtn:(id)sender;

@end
