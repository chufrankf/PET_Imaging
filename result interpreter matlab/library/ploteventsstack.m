function [] = ploteventsstack(bmatrix, startn, endn)
% Generates plots for start to end amount of events in the bmatrix
%   bmatrix   :   compiled matrix of all events
%   start     :   starting point for event reading
%   end       :   ending point for event reading

% check start and end for errors
nrows = size(bmatrix, 1);
if (isinteger(startn) || isinteger(endn))
    error('start and end are not integers');
end
if (startn > nrows || endn > nrows || startn > endn)
    error('start and end positions do not fit with %d rows', nrows);
end

% check to insure that many plots are desired
fprintf('\nPrinting from %d to %d\n', startn, endn);
check = input('Are you sure you want to plot that many? (yes/no)', 's');
if (strcmp(check,'no') || strcmp(check,'No') || strcmp(check, 'n') || strcmp(check,'N') || strcmp(check,'NO'))
    error('plotevents cancelled');
end

hold on
% check hold and check the events
for i = startn:endn
    plot(bmatrix(i,:))
    title(i);
    xlabel('time');
    ylabel('amplitude');
end
