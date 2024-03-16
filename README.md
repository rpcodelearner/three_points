# ThreePoints
Given 3 focal points, it plots curves such that the sum of the distances to the focal points is a constant.

## User's choices
You can select:
* How many focal points (aka "foci")
* How the foci are distributed in the window
* How to draw bands or lines of constant total distance from the foci.

See below for more details.

### Number of focal points
* The default is 3 foci, which gave the app its name
* Choosing 1 produces a circle
* Choosing 2 gives an ellipse
* there is no upper limit, but we recommend a gradual increase: the larger the number, the longer the app takes

### Foci Distribution
You have three choices for the distribution of the focal points:
* Circular: on a circle
* Random: randomly selected positions within the window
* Aligned: on a straight horizontal line

### Drawing method
The approach to drawing is very rough: the app scans each pixel in the window, computes the sum of the distances for that pixel, and then decides whether the pixel should be black (foreground) or white (background).
Choices:
  * Thick - range based: check if the value falls within a family of ranges (coarse grained version) 
  * Medium - as per Thick, but many smaller bands
  * Fine - as per Medium, but the bands are thinner
  * Precision - check if the value crosses a given "level" within the pixel being considered. It considers a number of such "levels", also depending on the number of foci. This method takes longer.
