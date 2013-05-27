using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Windows;
using System.Windows.Controls;
using System.Windows.Navigation;
using Microsoft.Phone.Controls;
using Microsoft.Phone.Shell;
using System.Windows.Threading;
using Microsoft.Devices.Sensors;
using Microsoft.Xna.Framework;

namespace ExerciseTracker
{
    public partial class RecordPage : PhoneApplicationPage
    {

        DispatcherTimer dispatcherTimer;
        Accelerometer accelSensor;
        int seconds = 0, minutes = 0, hours = 0, steps = 0, calories = 0;
        float CALORIES_PER_STEP = 0.05f;
        float STEP_THRESHOLD = 0.99f;
        float STEP_RESET_THRESHOLD = 0.95f;
        bool isPossiblyStepping = false;

        /// <summary>
        /// Constuctor initializes the dispatchertimer and accelsensor objects
        /// </summary>
        public RecordPage()
        {
            InitializeComponent();
            //init Timer
            dispatcherTimer = new System.Windows.Threading.DispatcherTimer();
            dispatcherTimer.Tick += dispatcherTimer_Tick;
            dispatcherTimer.Interval = new TimeSpan(0, 0, 1);

            //init Accelerometer
            accelSensor = new Accelerometer();
            accelSensor.CurrentValueChanged += accelSensor_CurrentValueChanged;
        }


        //delegate void SetTextBlockTextDelegate(TextBlock txtblk, string text);
        //void SetTextBlockText(TextBlock txtblk, string text)
        //{
        //    txtblk.Text = text;
        //}

        /// <summary>
        /// Delegate to help update the steps text on the Main UI Thread
        /// </summary>
        delegate void UpdateStepsDelegate();
        void UpdateSteps()
        {
            addStep();
        }

        /// <summary>
        /// Responsible for setting the text on steps and calories
        /// </summary>
        void updateMetrics()
        {
            stepsTxt.Text = steps.ToString();
            caloriesTxt.Text = calories.ToString();
        }

        /// <summary>
        /// Update the internal step counter and calories counter and call updateMetrics() to change UI
        /// </summary>
        void addStep()
        {
            steps++;
            calories = (int)(steps * CALORIES_PER_STEP);
            updateMetrics();
        }

        /// <summary>
        /// callback from accelerometer, will check for a change on the Y axis to make a prediction on a step from the user
        /// </summary>
        void accelSensor_CurrentValueChanged(object sender, SensorReadingEventArgs<AccelerometerReading> e)
        {
            Vector3 accelReading = e.SensorReading.Acceleration;

            if (Math.Abs(accelReading.Y) < STEP_RESET_THRESHOLD)
            {
                isPossiblyStepping = true;
            }
            else if (isPossiblyStepping && Math.Abs(accelReading.Y) > STEP_THRESHOLD)
            {
                isPossiblyStepping = false;
                Dispatcher.BeginInvoke(new UpdateStepsDelegate(UpdateSteps));
            }
        //    stepsTxt.Dispatcher.BeginInvoke(new
        //SetTextBlockTextDelegate(SetTextBlockText),
        //stepsTxt, accelReading.Y.ToString());

            


        }

        /// <summary>
        /// Resets all values on the UI and internal counters as well. Will start the dispatcherTimer and accelSensor
        /// </summary>
        private void runTimer()
        {
            stepsTxt.Text = "0";
            caloriesTxt.Text = "0";
            timeElaspedTxt.Text = "00:00:00";
            seconds = minutes = hours = 0;
            dispatcherTimer.Start();
            try
            {
                accelSensor.Start();
            }
            catch (AccelerometerFailedException e)
            {
                // the accelerometer couldn't be started.  No fun!
                System.Diagnostics.Debug.WriteLine(e.Message);
            }
            catch (UnauthorizedAccessException e)
            {
                // This exception is thrown in the emulator-which doesn't support an accelerometer.
                System.Diagnostics.Debug.WriteLine(e.Message);
            }
        }

        /// <summary>
        /// callback for dispatcherTimer. We know it will trigger every one second so count seconds, minutes and hours as appropriate.
        /// Update the timeElaspsed text on the UI as well.
        /// </summary>
        void dispatcherTimer_Tick(object sender, EventArgs e)
        {
            // simple way to count hours, minutes, and seconds

            seconds++;
            if (seconds == 60)
            {
                seconds = 0;
                minutes++;
            }

            if (minutes == 60)
            {
                minutes = 0;
                hours++;
            }
            //addStep();
            this.timeElaspedTxt.Text = String.Format("{0:00}:{1:00}:{2:00}", hours, minutes, seconds);
        }

        /// <summary>
        /// Play/Stop button press. Start recording time/steps/cals or Stop recording.
        /// </summary>
        private void ApplicationBarIconButton_Click_1(object sender, EventArgs e)
        {

            ApplicationBarIconButton recordBtn = (ApplicationBarIconButton)sender;
            System.Diagnostics.Debug.WriteLine("ApplicationBarIconButton_Click_1 with state: " + recordBtn.Text);
            if (recordBtn.Text == "Start") // easy way to understand current state
            {
                runTimer();
                recordBtn.Text = "Stop";
                recordBtn.IconUri = new Uri("/Assets/AppBar/appbar.stop.rest.png", UriKind.Relative);
            }
            else
            {
                dispatcherTimer.Stop();
                accelSensor.Stop();
                NavigationService.Navigate(new Uri("/SummaryPage.xaml?steps="+stepsTxt.Text+"&calories="+caloriesTxt.Text+"&time="+timeElaspedTxt.Text, UriKind.Relative));
                recordBtn.Text = "Start";
                recordBtn.IconUri = new Uri("/Assets/AppBar/appbar.transport.play.rest.png", UriKind.Relative);
            }
        }

    }
}