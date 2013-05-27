using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using ExerciseTracker.Models;

namespace ExerciseTracker
{
    public partial class SummaryPage : PhoneApplicationPage
    {
        bool fromHome = false;

        public SummaryPage()
        {
            InitializeComponent();
        }

        /// <summary>
        /// If from the home screen, set an internal variable. Also, get the steps, calories, and time from the url pass
        /// </summary>
        protected override void OnNavigatedTo(NavigationEventArgs e)
        {
            base.OnNavigatedTo(e);
            if (NavigationContext.QueryString.ContainsKey("fromHome"))
            {
                fromHome = true;
                ApplicationBar.Buttons.RemoveAt(0); // don't allow to save from the live tile -> summary transition
            }
            else if (NavigationContext.QueryString.ContainsKey("fromCal"))
            {

                ApplicationBar.Buttons.RemoveAt(0); // don't allow to save from the cal -> summary transition
            }
            totalStepsTxt.Text = NavigationContext.QueryString["steps"];
            totalCaloriesTxt.Text = NavigationContext.QueryString["calories"];
            totalTimeTxt.Text = NavigationContext.QueryString["time"];
        }

        /// <summary>
        /// If we are from Live Tile, then a back press should go back to main, otherwise back to the record screen
        /// </summary>
        protected override void OnBackKeyPress(System.ComponentModel.CancelEventArgs e)
        {
            base.OnBackKeyPress(e);
            if(fromHome)
                NavigationService.Navigate(new Uri("/MainPage.xaml", UriKind.Relative));
            else if (NavigationService.CanGoBack)
            {
                NavigationService.GoBack();
            }

        }

        /// <summary>
        /// Save button press adds this step session to the internal database and goes back to the mainpage
        /// </summary>
        private void ApplicationBarIconButton_Click_1(object sender, EventArgs e)
        {
            App.ViewModel.AddWorkoutItem(new WorkoutItem()
            {
                StepsTaken = totalStepsTxt.Text,
                CaloriesBurned = totalCaloriesTxt.Text,
                ElapsedTime = totalTimeTxt.Text,
                TimeTaken = DateTime.Parse(DateTime.Now.ToShortDateString())
            });
            NavigationService.Navigate(new Uri("/MainPage.xaml?save=1", UriKind.Relative));
        }

        /// <summary>
        /// Pin button press adds to the Home Screen, only one tile is maintained in this app, so delete any previous tiles.
        /// </summary>
        private void ApplicationBarIconButton_Click_2(object sender, EventArgs e)
        {
            // This application currently maintains only one live tile
            // Remove any previous live tiles by this application
            foreach (var activeTile in ShellTile.ActiveTiles)
            {
                if (activeTile.NavigationUri.OriginalString.Contains("="))
                    activeTile.Delete();
            }



            var newTile = new StandardTileData()
            {
                Title = "StepTracker",
                BackgroundImage = new Uri("ApplicationIcon.png", UriKind.Relative),
                BackContent = "Steps: " + totalStepsTxt.Text + Environment.NewLine + "Calories: " + totalCaloriesTxt.Text + Environment.NewLine + "Total Time: " + totalTimeTxt.Text,
                BackTitle = "Pinned Summary"


            };

            ShellTile.Create(new Uri("/SummaryPage.xaml?steps=" + totalStepsTxt.Text + "&calories=" + totalCaloriesTxt.Text + "&time=" + totalTimeTxt.Text + "&fromHome=1", UriKind.Relative), newTile);

        }
    }
}