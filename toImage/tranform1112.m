function [ma,ba,mb,bb,da,db,m,minv,x,inputarray,exit1,exit2,exit3] = tranform1112(input)

%equation line 1
ma=(input(5)-input(2))/(input(4)-input(1));
ba=input(5)-ma*input(4);
%equation line 2
mb=(input(11)-input(8))/(input(10)-input(7));
bb=input(11)-mb*input(10);
%distant between points
da=norm([input(4),input(5)]-[input(1),input(2)]);
db=norm([input(10),input(11)]-[input(7),input(8)]);
%matrix of equation of time
m=[1,0,0,0,1,0;0,1,0,0,1,0;0,0,1,0,0,1;0,0,0,1,0,1;1,1,0,0,0,0;0,0,1,1,0,0];
minv=inv(m);
%solution time
x=minv*[input(3);input(6);input(9);input(12);da/(3e11);db/(3e11)];
%solition thir lone
%input array is [x1, y1, t11, x2, y2, t22]
inputarray=[input(1),input(2),x(1),input(4),input(5),x(2)];
exit1=findPoint(inputarray);
inputarray=[input(7),input(8),x(3),input(10),input(11),x(4)];
exit2=findPoint(inputarray);
%middle point
inputarray=[exit1(1),exit1(2),x(5),exit2(1),exit2(2),x(6)];
exit3=findPoint(inputarray);
end

