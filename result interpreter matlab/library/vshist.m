function [vshist] = vshist(fname)
%vshist.m
%Generates histogram for absolute value of all voltages in input vector
%resolution is the smallest difference between sequential voltages
%number of bins is the number of x-axis ranges in the histogram, determined
%from the resolution
%   input  : event vector
%   output : [resolution, # of bins]
%   fname  :  column vector of data

%switch to positive voltages and sort unique values

absMaxes=abs(fname);
sortedAbsMaxes=sort(absMaxes);
uniqueSortedAbsMaxes=unique(sortedAbsMaxes);
numMaxes = length(uniqueSortedAbsMaxes);

%consider the smallest interval between any data points as the resolution
%first create a vector of intervals between sorted unique numbers

gapsMat = zeros(numMaxes-1,1);
for i = 1:numMaxes-1
    gapsMat(i) = uniqueSortedAbsMaxes(i+1) - uniqueSortedAbsMaxes(i);
end 

%find smallest "gap" and range to find bins
%resolution is minimum size of gaps
res = min(gapsMat);
range = max(absMaxes)-min(absMaxes);
%number of bins is the range/resoultion-1
nbin = round(range/res-1);

%create historgram vshist
phist = hist(absMaxes,nbin);

%create x and y values for custom histogram plot (allows multi-plot and
%labelling, among other customizations)

xax = [min(absMaxes)+res:res:max(absMaxes)-res]';
yax = phist';

disp(['length of x axis: ', num2str(length(xax))]);
length(['length of y axis: ', num2str(length(yax))]);

%h1=figure;
%semilogy(xax,yax,'k.',xax,yax,'b:') 
%xlabel('Voltage (V)')
%ylabel('Counts')
%title(['Histogram of data from file'])

%saveas(h,['figures\' fname '.fig'])
%saveas(h,['figures\' fname '.png'])

h2=figure;
axes('Parent',h2,'YScale','log','YMinorTick','on');
box('on');
hold('all');
bar(xax,yax)
xlabel('Voltage (V)')
ylabel('Counts')
title(['Histogram of data from file: '])

%saveas(h2,['figures\' fname 'b.fig'])
%saveas(h2,['figures\' fname 'b.png'])

%the following can be used to annotate specific data points
%text(4.32,584,'\leftarrow Photo peak?',...
%     'HorizontalAlignment','left')

%display values for resolution, minimum and maximum
disp('output = [res,nbin]');
disp(['number of unique values: ', num2str(numMaxes)]);
disp(['resolution: ', num2str(res)]);
disp(['minimum: ', num2str(min(absMaxes))]);
disp(['maximum: ', num2str(max(absMaxes))]);

%output
vshist = [res,nbin];
