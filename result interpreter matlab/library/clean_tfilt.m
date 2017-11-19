function [t_filter_res, filtloc] = clean_tfilt(inbmat)
%t_filter_res = cleanfilt(t_filt_mat)
%removes the zero matrices in the t_filter result
%t_filter_res  :  allbmatrix with the zeros applied
%t_filt_mat    :  input matrix that has been filtered

prompt ='Input Threshold: ';
t_num = input(prompt);

readbmat = cellabs(inbmat);

%creats cell of all thresholds with 0
t_filter_res = allthresh(readbmat, t_num);

%creates cell of vectors for location of each threshold
filtloc = cell(size(t_filter_res));

%removes 0 matrices from dirty threshold and finds locations for each
for row = 1:size(t_filter_res,1)
    for col = 1:size(t_filter_res,2)
        t_filter_res{row,col} = t_filter_res{row,col}(any(t_filter_res{row,col},2),:);
        filtloc{row,col} = find(bmax(readbmat{row,col}) > t_num);
    end
end

fprintf('\noutput is[t_filter_res, filtloc]\nwhere\nt_filter_res: resulting matrix after threshold filtering\nfiltloc: number (time unit) of the event in the data set that passed \nthe filter (used mainly for coincidence check)\n');

fprintf('\nif you did not set the result to a variable,\n t_filter_res is located in "ans"\n');




