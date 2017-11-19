function [maxes,index] = bmaxind(bmatrix)
%finds maximums and their indexs

nrows = size(bmatrix,1);
%initializes matrix
maxes = zeros(nrows,1);
index = zeros(nrows,1);

%computes maxes for each event
for k = 1:nrows
    if(size(bmatrix,2) > 0)
        [maxes(k), index(k)] = max(bmatrix(k,:));
        
    end
end
