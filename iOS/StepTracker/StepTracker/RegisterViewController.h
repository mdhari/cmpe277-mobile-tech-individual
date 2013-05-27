//
//  RegisterViewController.h
//  StepTracker
//
//  Created by Michael Hari on 4/14/13.
//  Copyright (c) 2013 San Jose State University. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface RegisterViewController : UIViewController
- (IBAction)submitBtn:(id)sender;
- (IBAction)cancelBtn:(id)sender;
@property (strong, nonatomic) IBOutlet UITextField *username;
@property (strong, nonatomic) IBOutlet UITextField *password;

@end
