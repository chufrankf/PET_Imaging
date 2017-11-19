function point = findPoint(inputArray)

%input array is [x1, y1, t11, x2, y2, t22]
%point is [xp, yp]

SPEED = 3*10^11; %units [mm/s]

x1 = inputArray(1);
x2 = inputArray(4);
y1 = inputArray(2);
y2 = inputArray(5);
t1 = inputArray(3);
t2 = inputArray(6);

magnitude = sqrt((x2-x1)^2+(y2-y1)^2);
timeCent = magnitude/(2*SPEED);

dt = (t1-timeCent);
dD = dt*SPEED;

if(x1 ~= x2)
    slope = (y2-y1)/(x2-x1);
    angle = atand(slope);
    if slope >=0
    x = (x1+x2)/2+dD*cosd(angle);
    y = (y1+y2)/2+dD*sind(angle);
    else
    x = ((x1+x2)/2-dD*cosd(angle));
    y = (y1+y2)/2-dD*sind(angle);    
    end
else
    x = x1;
    y = (y1+y2)/2-dD; 
end

point = [x,y];


