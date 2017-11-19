function tbmatrix = allthresh(allbmatrix, threshnum)
%finds threshold for all matrices in all bmatrix and keeps only those that
%pass threshold
%tbmatrix  :  ouput cell with only events that pass threshold
%allbmatrix:  input cell with all events
%threshold :  lower limit amount

tbmatrix = cell(size(allbmatrix));

%do threshold check for all in allbmatrix
for row = 1 : size(allbmatrix,1)
    for col = 1 : size(allbmatrix,2)
    fprintf('\n Data file: %d, %d \n', row, col);
    
        if(size(allbmatrix{row,col},2)>0)
            tbmatrix{row,col} = thresh(allbmatrix{row,col}, threshnum);
        end
    end
    
end