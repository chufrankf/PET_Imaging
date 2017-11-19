function brise = findrisemat(inbmat)
%finds the rise times for the batch matrix

[maxes, indexes] = bmaxind(inbmat);

if(size(inbmat,1) ~= size(maxes,1) || size(inbmat,1) ~= size(indexes,1))
   error('matrix sizes do not match'); 
end

brise = zeros(size(inbmat,1),1);


for i=1:size(inbmat,1)
    
    index = indexes(i);
    t1 = index;
    t2 = index;
    volt = maxes(i);
    
    while (volt >= .1*maxes(i) && index>1)
        
        index = index -1;
        volt = inbmat(i,index);
        
        if(volt >= .9*maxes(i))
            t1 = index;
        end
        
        if(volt >= .1*maxes(i))
            t2 = index;
        end
        
    end
    
    brise(i) = t1 - t2;
    
end

