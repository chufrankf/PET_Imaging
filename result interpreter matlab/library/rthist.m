function [] = rthist(fname)

avp=abs(fname);
avps=sort(avp);
avpsu=unique(avps);
numunique = length(avpsu);

%consider the smallest interval between any data points as the resolution
%first create a vector of intervals between sorted unique numbers

gaps = zeros(numunique-1,1);
for i = 1:numunique-1
    gaps(i) = avpsu(i+1) - avpsu(i);
end 

%find smallest "gap" and range to find bins
res = min(gaps);
rng = max(avp)-min(avp);
nbin = round(rng/res-1);

%create historgram vshist
phist = hist(avp,nbin);

%create x and y values for custom histogram plot (allows multi-plot and
%labelling, among other customizations)

xax = [min(avp)+res:res:max(avp)-res]';
yax = phist';

disp(['length of x axis: ', num2str(length(xax))]);
length(['length of y axis: ', num2str(length(yax))]);

h2=figure;
axes('Parent',h2,'YScale','log','YMinorTick','on');
box('on');
hold('all');
bar(xax,yax);
xlabel('Time (units)')
ylabel('Counts')
title(['Histogram of data from file: ']);

disp('output = [res,nbin]');
disp(['number of unique values: ', num2str(numunique)]);
disp(['resolution: ', num2str(res)]);
disp(['minimum: ', num2str(min(avp))]);
disp(['maximum: ', num2str(max(avp))]);

