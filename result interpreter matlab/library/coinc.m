function coin_cell = coinc(inbmat)
%compares the A and B values in inbmat
%output is a cell: coin_cell = {num_coin,avolt,bvolt,time_loc,time_diff}
%outputs the number of coincidence, A vs B voltages, time of coincidence, time difference within
%event 
%inbmat  :  input cell of all batch matrices
%coin_mat:  ouput matrix with A,B voltages, time of coincidence, and time
%difference

if(size(inbmat,2) ~= 2)
    error('Cannot do coincidence of matrix without only 2 voltages');
end

[mfilt_bmat,mloc_bmat] = clean_tfilt(inbmat);

fprintf('\nComparing A and B only\n');
fprintf('Size of matrix is %d,%d\n', size(mfilt_bmat,1),size(mfilt_bmat,2));

num_coin = zeros(size(mfilt_bmat,1),1);
avolt = zeros();
bvolt = zeros();
time_loc = zeros();
time_diff = zeros();

%Cell
for row = 1:size(mloc_bmat,1)
    %matrix A
    for rA = 1:size(mloc_bmat{row,1},1);
        %matrix B
        for rB = 1:size(mloc_bmat{row,2},1);
            if(mloc_bmat{row,1}(rA,1) == mloc_bmat{row,2}(rB,1))
                %add to coincidence counter
                num_coin(row,1) = num_coin(row,1) + 1;
                %find a volt
                [avolt(row,num_coin(row,1)),a_time] = max(mfilt_bmat{row,1}(rA,:));
                %find b volt
                [bvolt(row,num_coin(row,1)),b_time] = max(mfilt_bmat{row,2}(rB,:));
                %record location
                time_loc(row,num_coin(row,1)) = mloc_bmat{row,1}(rA,1);
                %record time diff
                time_diff(row,num_coin(row,1)) = abs(a_time - b_time);

            end
        end
    end
        
end

coin_cell = {num_coin,avolt,bvolt,time_loc,time_diff};

fprintf('\noutput is a cell: coin_cell = {num_coin,avolt,bvolt,time_loc,time_diff}\n where\nnum_coin: number of coincidences within each data set in your folder \navolt: is the voltage height for A\nbvolt: is the voltage height for B\ntime_loc: The number(unit time) of the event that this took place \ntime_diff: difference in unit time that the two pulses are located.\n');
fprintf('\nif you did not set the result to a variable,\n result cell is located in "ans"\n');


