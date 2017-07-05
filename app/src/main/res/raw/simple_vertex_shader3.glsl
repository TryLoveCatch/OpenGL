attribute vec4 a_Position;
attribute vec2 a_Coordinate;

varying vec2 v_Coordinate;

void main()
{
  v_Coordinate = a_Coordinate;
  gl_Position = a_Position;
}