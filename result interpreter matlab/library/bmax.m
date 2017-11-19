function maxes = bmax(bmatrix)
%bmax.m
%Andrew Jastram
%2014-03-25
%Generates vector of maxes for each event
%   maxes  :  matrix of maxes
%   vecvic :  batched matrix of events

nrows = size(bmatrix,1);
%initializes matrix
maxes = zeros(nrows,1);

%computes maxes for each event
for k = 1:nrows
    if(size(bmatrix,2) > 0)
        maxes(k)=abs(max(bmatrix(k,:))); 
    end
end
