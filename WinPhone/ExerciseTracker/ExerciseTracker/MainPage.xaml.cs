using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Documents;
using System.Windows.Input;
using System.Windows.Media;
using System.Windows.Media.Animation;
using System.Windows.Shapes;
using Microsoft.Phone.Controls;
using ExerciseTracker.Models;

namespace ExerciseTracker
{
    public partial class MainPage : PhoneApplicationPage
    {
        // Constructor
        public MainPage()
        {
            InitializeComponent();

            // Set the data context of the listbox control to the sample data
            //DataContext = App.ViewModel;
            //this.Loaded += new RoutedEventHandler(MainPage_Loaded);
        }

        /// <summary>
        /// Will remove any screens on the stack, also will advance the panorama if navigated from the summary save button press
        /// </summary>
        protected override void OnNavigatedTo(System.Windows.Navigation.NavigationEventArgs e)
        {
            base.OnNavigatedTo(e);
            // Remove all back entries, so that pressing back from here guarantees exiting from app
            while (NavigationService.BackStack.Any())
                NavigationService.RemoveBackEntry();

            if(NavigationContext.QueryString.Count() > 0) // set progess as default screen if from save button
                panoramaControl.DefaultItem = panoramaControl.Items[1];
        }

        /// <summary>
        /// Will navigate to record screen
        /// </summary>
        private void recordWorkoutGrid_Tap(object sender, GestureEventArgs e)
        {
            System.Diagnostics.Debug.WriteLine("recordWorkoutGrid_Tap");
            NavigationService.Navigate(new Uri("/RecordPage.xaml", UriKind.Relative));
        }

        //private void settingsGrid_Tap(object sender, GestureEventArgs e)
        //{
        //    System.Diagnostics.Debug.WriteLine("settingsGrid_Tap");
        //    NavigationService.Navigate(new Uri("/SettingsPage.xaml", UriKind.Relative));
        //}

        // MH: WPControl Event Handlers
        /// <summary>
        /// Will transfer to the summary page with information from the model/database on a selected date, otherwise show
        /// just a message box saying no steps were recorded that day
        /// </summary>
        private void Cal_DateClicked(object sender, WPControls.SelectionChangedEventArgs e)
        {
            System.Diagnostics.Debug.WriteLine("Cal_DateClicked fired.  New date is " + e.SelectedDate.ToString());
            WorkoutItem workoutItem = App.ViewModel.FindWorkoutItem(e.SelectedDate);
            if (workoutItem != null)
            {
                NavigationService.Navigate(new Uri("/SummaryPage.xaml?steps=" + workoutItem.StepsTaken + "&calories=" + workoutItem.CaloriesBurned + "&time=" + workoutItem.ElapsedTime + "&fromCal=1", UriKind.Relative));
            }
            else
            {
                MessageBox.Show("No workout recorded for this day");
            }
        }

    }
}