//
//  RegisterViewController.m
//  StepTracker
//
//  Created by Michael Hari on 4/14/13.
//  Copyright (c) 2013 San Jose State University. All rights reserved.
//

#import "RegisterViewController.h"
#import "SFHFKeychainUtils.h"


@interface RegisterViewController ()

@end

@implementation RegisterViewController

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

-(void) displayAlert:(NSString *)_message{
    
    UIAlertView *alertView = [[UIAlertView alloc]initWithTitle:@"Information" message:_message delegate:self cancelButtonTitle:@"OK" otherButtonTitles:nil, nil];
    [alertView show];
}

- (BOOL) validateUsername:(NSString*)username andPassword:(NSString*)password{
    
    if([username length] == 0){
        [self displayAlert:@"Username cannot be empty"];
        return NO;
    }
    if([password length] == 0){
        [self displayAlert:@"Password cannot be empty"];
        return NO;
    }
    NSError *error = nil;
    NSString *usersPassword = [SFHFKeychainUtils getPasswordForUsername:username andServiceName:@"StepTrackerLogin" error:&error];
    if(error != nil){
        NSLog(@"error: %@", [error localizedDescription]);
        [self displayAlert:[error localizedDescription]];
        return NO;
    }
    
    if(usersPassword != nil){
        [self displayAlert:@"Username already exists!"];
        return NO;
    }
    
    NSError *error2 = nil;
    [SFHFKeychainUtils storeUsername:username andPassword:password forServiceName:@"StepTrackerLogin" updateExisting:NO error:&error2];
    if(error2 != nil){
        NSLog(@"error: %@", [error2 localizedDescription]);
        [self displayAlert:[error2 localizedDescription]];
        return NO;
    }
    
    return YES;
    
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

-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event{
    [self.view endEditing:YES];
}

- (BOOL)textFieldShouldReturn:(UITextField *)theTextField {
    if (theTextField == _password) {
        [theTextField resignFirstResponder];
        [self submitBtn:self];
    } else if (theTextField == _username) {
        [_password becomeFirstResponder];
    }
    return YES;
}

- (IBAction)submitBtn:(id)sender {
    
    if([self validateUsername:[_username text] andPassword:[_password text]]){
        [self dismissViewControllerAnimated:YES completion:nil];
    }
}

- (IBAction)cancelBtn:(id)sender {
    [self dismissViewControllerAnimated:YES completion:nil];
}
- (void)viewDidUnload {
    [self setUsername:nil];
    [self setPassword:nil];
    [super viewDidUnload];
}
@end
