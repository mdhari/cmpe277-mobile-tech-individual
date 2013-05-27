using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data.Linq;
using System.Data.Linq.Mapping;
using System.Linq;
using System.Text;

namespace ExerciseTracker.Models
{
    public class StepTrackerDataContext : DataContext
    {
        // Pass the connection string to the base class.
        public StepTrackerDataContext(string connectionString)
            : base(connectionString)
        { }

        // Specify a table for the workout items.
        public Table<WorkoutItem> Items;

    }

    [Table]
    public class WorkoutItem : INotifyPropertyChanged, INotifyPropertyChanging
    {

        // Define ID: private field, public property, and database column.
        private int _toDoItemId;

        [Column(IsPrimaryKey = true, IsDbGenerated = true, DbType = "INT NOT NULL Identity", CanBeNull = false, AutoSync = AutoSync.OnInsert)]
        public int ToDoItemId
        {
            get { return _toDoItemId; }
            set
            {
                if (_toDoItemId != value)
                {
                    NotifyPropertyChanging("ToDoItemId");
                    _toDoItemId = value;
                    NotifyPropertyChanged("ToDoItemId");
                }
            }
        }

        // Define the steps taken value: private field, public property and database column
        private string _stepsTaken;

        [Column]
        public string StepsTaken
        {
            get { return _stepsTaken; }
            set{
                if(_stepsTaken != value)
                {
                    NotifyPropertyChanging("StepsTaken");
                    _stepsTaken = value;
                    NotifyPropertyChanged("StepsTaken");
                }
            }
        }

        // Define the calories burned value: private field, public property and database column
        private string _caloriesBurned;

        [Column]
        public string CaloriesBurned
        {
            get { return _caloriesBurned; }
            set
            {
                if (_caloriesBurned != value)
                {
                    NotifyPropertyChanging("CaloriesBurned");
                    _caloriesBurned = value;
                    NotifyPropertyChanged("CaloriesBurned");
                }
            }
        }

        // Define the elasped time value: private field, public property and database column
        private string _elaspedTime;

        [Column]
        public string ElapsedTime
        {
            get { return _elaspedTime; }
            set
            {
                if (_elaspedTime != value)
                {
                    NotifyPropertyChanging("ElapsedTime");
                    _elaspedTime = value;
                    NotifyPropertyChanged("ElapsedTime");
                }
            }
        }

        // Define the time taken value: private field, public property and database column
        private DateTime _timeTaken;

        [Column]
        public DateTime TimeTaken
        {
            get { return _timeTaken; }
            set
            {
                if (_timeTaken != value)
                {
                    NotifyPropertyChanging("TimeTaken");
                    _timeTaken = value;
                    NotifyPropertyChanged("TimeTaken");
                }
            }
        }

        // Version column aids update performance.
        [Column(IsVersion = true)]
        private Binary _version;


        #region INotifyPropertyChanged Members

        public event PropertyChangedEventHandler PropertyChanged;

        // Used to notify that a property changed
        private void NotifyPropertyChanged(string propertyName)
        {
            if (PropertyChanged != null)
            {
                PropertyChanged(this, new PropertyChangedEventArgs(propertyName));
            }
        }

        #endregion

        #region INotifyPropertyChanging Members

        public event PropertyChangingEventHandler PropertyChanging;

        // Used to notify that a property is about to change
        private void NotifyPropertyChanging(string propertyName)
        {
            if (PropertyChanging != null)
            {
                PropertyChanging(this, new PropertyChangingEventArgs(propertyName));
            }
        }

        #endregion
    }
}
