function [] = graphVoltHist(inbmat,dataFileNamesmat)
%creats the histogram of the A and B voltages of each bmatrix in all bmatrix


for i=1:size(inbmat,1)
    for j=1:size(inbmat,2)
        
        fprintf('Getting Histogram for (%d,%d) \n', i, j);
        
        if(size(inbmat{i,j},2) > 0)
            
            maxmat = bmax(inbmat{i,j});
            
            resbin = vshist(maxmat);
            titlePart = sprintf(' %d bins, (%d,%d), Res: %d', resbin(1,2), i, j, resbin(1,1));
            titleString = strcat(dataFileNamesmat{i}, ' ', titlePart);
            
            title(titleString);
            
        end
        
    end
end

