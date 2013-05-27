//
//  WorkoutSummary.h
//  StepTracker
//
//  Created by Michael Hari on 4/13/13.
//  Copyright (c) 2013 San Jose State University. All rights reserved.
//

#import <Foundation/Foundation.h>
#import <CoreData/CoreData.h>


@interface WorkoutSummary : NSManagedObject

@property (nonatomic, retain) NSDate * workout_date;
@property (nonatomic, retain) NSNumber * calories;
@property (nonatomic, retain) NSNumber * steps;
@property (nonatomic, retain) NSString * timeElapsed;
@property (nonatomic, retain) NSString * username;

@end
