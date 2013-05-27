//
//  StepTrackerAppDelegate.h
//  StepTracker
//
//  Created by Michael Hari on 4/4/13.
//  Copyright (c) 2013 San Jose State University. All rights reserved.
//

#import <UIKit/UIKit.h>

@interface StepTrackerAppDelegate : UIResponder <UIApplicationDelegate>

@property (strong, nonatomic) UIWindow *window;

@property (readonly, strong, nonatomic) NSManagedObjectContext *managedObjectContext;
@property (readonly, strong, nonatomic) NSManagedObjectModel *managedObjectModel;
@property (readonly, strong, nonatomic) NSPersistentStoreCoordinator *persistentStoreCoordinator;

- (void)saveContext;
- (NSURL *)applicationDocumentsDirectory;

@end
