using ExerciseTracker;
using System;
using System.Windows.Media;
using WPControls;

namespace WpControlsExample
{
    public class ColorConverter : IDateToBrushConverter
    {

        public Brush Convert(DateTime dateTime, bool isSelected, Brush defaultValue, BrushType brushType)
        {
            if (brushType != BrushType.Background)
            {
                if (App.ViewModel.FindWorkoutItem(dateTime) != null)
                {
                    return new SolidColorBrush(Colors.Yellow);
                }
                else
                {
                    return defaultValue;
                }
            }

            return defaultValue;

        }
    }
}
