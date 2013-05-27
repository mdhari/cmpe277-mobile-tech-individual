//
//  WorkoutsViewController.m
//  StepTracker
//
//  Created by Michael Hari on 4/13/13.
//  Copyright (c) 2013 San Jose State University. All rights reserved.
//

#import "WorkoutsViewController.h"
#import "StepTrackerAppDelegate.h"
#import "WorkoutSummary.h"

@interface WorkoutsViewController ()

@end

@implementation WorkoutsViewController
@synthesize workoutsArray;
@synthesize loggedInUsername;

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
     NSLog(@"LoggedInUserName: %@",self.loggedInUsername);
    [self setWorkoutArray];

}

-(void) setWorkoutArray{
    StepTrackerAppDelegate *appDelegate = (StepTrackerAppDelegate *)[[UIApplication sharedApplication] delegate];
    NSFetchRequest *request = [[NSFetchRequest alloc] init];
    NSEntityDescription *entity = [NSEntityDescription entityForName:@"WorkoutSummary" inManagedObjectContext:appDelegate.managedObjectContext];
    [request setEntity:entity];
    NSSortDescriptor *sortDescriptor = [[NSSortDescriptor alloc] initWithKey:@"workout_date" ascending:NO];
    NSArray *sortDescriptors = [[NSArray alloc] initWithObjects:sortDescriptor, nil];
    [request setSortDescriptors:sortDescriptors];
    NSPredicate *predicate = [NSPredicate predicateWithFormat:@"username=%@", self.loggedInUsername];
    [request setPredicate:predicate];
    NSError *error = nil;
    NSMutableArray *mutableFetchResults = [[appDelegate.managedObjectContext executeFetchRequest:request error:&error] mutableCopy];
    if (mutableFetchResults == nil) {
        NSLog(@"fetch is nil");
    }
    [self setWorkoutsArray:mutableFetchResults];
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

- (NSInteger)numberOfSectionsInTableView:(UITableView *)tableView
{
    return 1;
}

- (NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    NSLog(@"numberOfSections: %d",[workoutsArray count]);
    return [workoutsArray count];
}

- (UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{

    WorkoutSummary *workoutSummary = [workoutsArray objectAtIndex:indexPath.row];
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:@"workout"];
    
    UILabel *label;

    static NSDateFormatter *dateFormatter = nil;
    if (dateFormatter == nil) {
        dateFormatter = [[NSDateFormatter alloc] init];
        [dateFormatter setTimeStyle:NSDateFormatterMediumStyle];
        [dateFormatter setDateStyle:NSDateFormatterMediumStyle];
    }
    label = (UILabel *)[cell viewWithTag:1];
    label.text = [NSString stringWithFormat:@"%@", [dateFormatter stringFromDate:workoutSummary.workout_date]];
    
    label = (UILabel *)[cell viewWithTag:2];
    label.text = [NSString stringWithFormat:@"Steps: %03d", [workoutSummary.steps intValue]];
    
    label = (UILabel *)[cell viewWithTag:3];
    label.text = [NSString stringWithFormat:@"Calories: %03d", [workoutSummary.calories intValue]];
    
    label = (UILabel *)[cell viewWithTag:4];
    label.text = [NSString stringWithFormat:@"Time Elasped: %@", workoutSummary.timeElapsed];
    
    return cell;
}

- (void)tableView:(UITableView *)tableView commitEditingStyle:(UITableViewCellEditingStyle)editingStyle forRowAtIndexPath:(NSIndexPath *)indexPath {
    
    if (editingStyle == UITableViewCellEditingStyleDelete) {
        StepTrackerAppDelegate *appDelegate = (StepTrackerAppDelegate *)[[UIApplication sharedApplication] delegate];
        // Delete the managed object at the given index path.
        NSManagedObject *workoutToDelete = [workoutsArray objectAtIndex:indexPath.row];
        [appDelegate.managedObjectContext deleteObject:workoutToDelete];
        
        // Update the array and table view.
        [workoutsArray removeObjectAtIndex:indexPath.row];
        [tableView deleteRowsAtIndexPaths:[NSArray arrayWithObject:indexPath] withRowAnimation:YES];
        
        // Commit the change.
        NSError *error = nil;
        if (![appDelegate.managedObjectContext save:&error]) {
            NSLog(@"error deleting");
        }
    }
}

#pragma mark - alertView delegate

// delete all workouts?
- (void)alertView:(UIAlertView *)alertView clickedButtonAtIndex:(NSInteger)buttonIndex{
    // 0 = No, 1 = Yes
    if(buttonIndex==1){
        StepTrackerAppDelegate *appDelegate = (StepTrackerAppDelegate *)[[UIApplication sharedApplication] delegate];
        // Delete the managed object at the given index path.
        for(int i = 0 ; i < workoutsArray.count;i++){
            [appDelegate.managedObjectContext deleteObject:[workoutsArray objectAtIndex:i]];
        }
        
        // Commit the change.
        NSError *error = nil;
        if (![appDelegate.managedObjectContext save:&error]) {
            NSLog(@"error deleting");
        }
        
        [workoutsArray removeAllObjects];
        
        // Update the array and table view.
        [self setWorkoutArray];
        [self.tableView reloadData];
        
        
    }
    
    
    
}

- (IBAction)trashBtn:(id)sender {
    UIAlertView *alertView = [[UIAlertView alloc]initWithTitle:@"Confirmation" message:@"Are you sure you want to delete all workouts?" delegate:self cancelButtonTitle:@"No" otherButtonTitles:@"Yes", nil];
    [alertView show];
}
@end
