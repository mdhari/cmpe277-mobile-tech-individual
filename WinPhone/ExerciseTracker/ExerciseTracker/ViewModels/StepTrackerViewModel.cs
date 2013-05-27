using ExerciseTracker.Models;
using System;
using System.Collections.Generic;
using System.Collections.ObjectModel;
using System.ComponentModel;
using System.Linq;
using System.Text;

namespace ExerciseTracker.ViewModels
{
    public class StepTrackerViewModel : INotifyPropertyChanged
    {
        // LINQ to SQL data context for the local database.
        private StepTrackerDataContext stepTrackerDB;

        // Class constructor, create the data context object.
        public StepTrackerViewModel(string toDoDBConnectionString)
        {
            stepTrackerDB = new StepTrackerDataContext(toDoDBConnectionString);
        }

        // All workouts, used by calendar
        private ObservableCollection<WorkoutItem> _workoutItems;
        public ObservableCollection<WorkoutItem> WorkoutItems
        {
            get { return _workoutItems; }
            set
            {
                _workoutItems = value;
                NotifyPropertyChanged("WorkoutItems");
            }
        }

        // Write changes in the data context to the database.
        public void SaveChangesToDB()
        {
            stepTrackerDB.SubmitChanges();
        }

        // Query database and load the collections and list used by the pivot pages.
        public void LoadCollectionsFromDatabase()
        {

            // Specify the query for all to-do items in the database.
            var workoutItemsInDB = from WorkoutItem workout in stepTrackerDB.Items
                                select workout;

            // Query the database and load all to-do items.
            WorkoutItems = new ObservableCollection<WorkoutItem>(workoutItemsInDB);

            //foreach (var item in workoutItemsInDB)
            //{
            //    System.Diagnostics.Debug.WriteLine(item.TimeTaken);
            //}
         

        }

        public WorkoutItem FindWorkoutItem(DateTime date)
        {
            DateTime dateWithoutTime = DateTime.Parse(date.ToShortDateString());
            
            var workoutItemsInDB = from WorkoutItem workout in stepTrackerDB.Items
                                   where workout.TimeTaken == dateWithoutTime
                                   select workout;
            if (workoutItemsInDB.Count() > 0)
            {
                //System.Diagnostics.Debug.WriteLine(dateWithoutTime);
                return workoutItemsInDB.First();
            }
            return null;
        }

        // Add a to-do item to the database and collections.
        public void AddWorkoutItem(WorkoutItem newWorkoutItem)
        {
            // this app maintains only one workout per day, hence if there is a second one, delete it
            var workoutItemsInDB = from WorkoutItem workout in stepTrackerDB.Items
                                   where workout.TimeTaken == newWorkoutItem.TimeTaken
                                   select workout;

            foreach(var item in workoutItemsInDB){
                DeleteWorkoutItem(item);
            }

            
            // Add a to-do item to the data context.
            
            stepTrackerDB.Items.InsertOnSubmit(newWorkoutItem);

            // Save changes to the database.
            stepTrackerDB.SubmitChanges();

            // Add a to-do item to the "all" observable collection.
            WorkoutItems.Add(newWorkoutItem);

            
        }

        // Remove a to-do task item from the database and collections.
        public void DeleteWorkoutItem(WorkoutItem workoutForDelete)
        {

            // Remove the to-do item from the "all" observable collection.
            WorkoutItems.Remove(workoutForDelete);

            // Remove the to-do item from the data context.
            stepTrackerDB.Items.DeleteOnSubmit(workoutForDelete);

            // Save changes to the database.
            stepTrackerDB.SubmitChanges();
        }


        #region INotifyPropertyChanged Members

        public event PropertyChangedEventHandler PropertyChanged;

        // Used to notify Silverlight that a property has changed.
        private void NotifyPropertyChanged(string propertyName)
        {
            if (PropertyChanged != null)
            {
                PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
            }
        }
        #endregion
    }
}
