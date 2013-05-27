//
//  WorkoutsViewController.h
//  StepTracker
//
//  Created by Michael Hari on 4/13/13.
//  Copyright (c) 2013 San Jose State University. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface WorkoutsViewController : UITableViewController{
    NSMutableArray *workoutsArray;
}
- (IBAction)trashBtn:(id)sender;

@property(nonatomic,retain) NSMutableArray *workoutsArray;
@property(strong,nonatomic) NSString *loggedInUsername;
@end
