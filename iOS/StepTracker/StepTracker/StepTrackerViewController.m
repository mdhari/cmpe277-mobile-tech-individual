//
//  StepTrackerViewController.m
//  StepTracker
//
//  Created by Michael Hari on 4/4/13.
//  Copyright (c) 2013 San Jose State University. All rights reserved.
//

#import "StepTrackerViewController.h"
#import "SFHFKeychainUtils.h"
#import "MainMenuViewController.h"

@interface StepTrackerViewController ()

@end

@implementation StepTrackerViewController

- (void)viewDidLoad
{
    [super viewDidLoad];
	// Do any additional setup after loading the view, typically from a nib.
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

- (BOOL)textFieldShouldReturn:(UITextField *)theTextField {
    if (theTextField == _password) {
        [theTextField resignFirstResponder];
        [self loginBtn:self];
    } else if (theTextField == _username) {
        [_password becomeFirstResponder];
    }
    return YES;
}

-(void)touchesBegan:(NSSet *)touches withEvent:(UIEvent *)event{
    [self.view endEditing:YES];
}


- (void)viewDidUnload {
    [self setUsername:nil];
    [self setPassword:nil];
    [super viewDidUnload];
}

- (void)prepareForSegue:(UIStoryboardSegue *)segue sender:(id)sender{
    if(![[segue identifier] isEqualToString:@"registerSegue"]){
        MainMenuViewController *mainMenuViewController = [segue destinationViewController];
        [mainMenuViewController setLoggedInUsername:[_username text]];
    }
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

- (IBAction)loginBtn:(id)sender {
    NSError *error = nil;
    NSString *storedPassword = [SFHFKeychainUtils getPasswordForUsername:[_username text] andServiceName:@"StepTrackerLogin" error:&error];
    if(error != nil){
        NSLog(@"error: %@",[error localizedDescription]);
        [self displayAlert:[error localizedDescription]];
    }
    
    if(storedPassword == nil){
        [self displayAlert:@"Username doesn't exist"];
        return;
    }
    
    //NSLog(@"storedPassword: %@ password entered: %@",storedPassword,[_password text]);
    if([storedPassword isEqualToString:[_password text]]){
        _password.text=@"";
        [_password resignFirstResponder];
        [self performSegueWithIdentifier:@"loginSegue" sender:self];
    }else{
        [self displayAlert:@"Username or password isn't correct"];
    }
    
}
@end
