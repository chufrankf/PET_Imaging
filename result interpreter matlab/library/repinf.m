function outbmat = repinf(inbmat, value)
%Replaces all Inf values with value
%outbmat = repinf(inbmat, value)
%outbmat  :  output bmat
%inbmat   :  input bmat
%value    :  what to replace inf with

outbmat = cell(size(inbmat));

for row = 1:size(inbmat,1)
    for col = 1:size(inbmat,2) 
        wirebmat = inbmat{row,col};
        wirebmat(wirebmat == Inf) = value;
        wirebmat(wirebmat == -Inf) = (-1)*value;
        outbmat{row,col} = wirebmat;
    end
end


