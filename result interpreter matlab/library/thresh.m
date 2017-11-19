function tbmatrix = thresh(bmatrix, threshold)
% Removes all events under threshold, replaces with 0 vector. Keeps events
% more than or equal to threshold
% tbmatrix  :  output matrix with 0 vectors for events that do not cross
% threshold
% bmatrix   :  input matrix batched matrix from bload
% threshold :  double specifying what is the minimum value needed to be
% accounted

%creates vector of maxes
maxes = bmax(bmatrix);

%counts number that passes threshold
countpass = 0;

%creates vector fo binary (1/0) pass or dont pass maxes
bithresh = zeros(size(bmatrix));
for i = 1:size(maxes,1)
    if(maxes(i) >= threshold)
        bithresh(i,:) = 1;
        countpass = countpass + 1;
    else
        bithresh(i,:) = 0;
    end
end

fprintf('Number of events that pass threshold: %d \n', countpass);

%mutiply element by element to zero all undesired events.
tbmatrix = bithresh.*bmatrix;

