...README...

-----!!!ALWAYS!!!-----
1.) do read.m (>> read)! OR invRead (>> invRead) % does inverse of matrix
	- Creates cell of batched matrices
	- Stores library and Data files

    OUTPUT:
    initallbmat: Raw data (no changes) 
    allbmatrix: Matrix After removing/replacing/ignoring infinite values
    names: Names of Data folders you placed in read Directory (use for graphVoltHist and graphRiseHist)

OR

1.) do doall.m (>> doall)! OR invDoall (>> invDoall) % does inverse of matrix
    - Creates cell of batched matrices
    - Stores library and Data files
    - Does read, shift, and clean_tfilt

    OUTPUT:
    initallbmat: Raw data (no changes) 
    allbmatrix: Matrix After removing/replacing/ignoring infinite values 
    names: Names of Data folders you placed in read Directory (use for graphVoltHist and graphRiseHist) 
    sbmat: Cell after shift has been made (no threshold) 
    tfiltbmat: Cell with threshold filtering with no prior shift 
    tsfiltbmat: Cell with threshold filtering after a shift

-----End Always-----

Notes:
inputcell is a matrix of matrices.
This matrix represents your folder directory.
The matrixes inside these cells are the data sets with all events
Examples: Allbmatrix, initallbmat, sbmat, tfiltmat, tsfiltmat, ...

2.) >> sbmat = shift(inputcell)
	- shifts values in cell
	- All batch matrices, single batch matrix, auto shift

3.) >> graphVoltHist(inputcell, names)
    - graphs the voltage histograms of all the 

4.) >> graphRiseHist(inputcell, names)
    - graphs the rise time histograms of all the 

5.) >> tfiltmat = clean_tfilt(inputcell)
	- OUPUT IS [filtered_bmat,loc_filt] = clean_tfilt(inputcell)
	- filtered_bmat  :  cell with filtered values
	- loc_filt       :  cell with timing location values

6.) >> coinc_result = coinc(inputcell)
	- INCLUDES clean_tfilt, does threshold check inside
	- OUTPUT IS A CELL
	- coin_cell = {num_coin,avolt,bvolt,time_loc,time_diff};
	- num coin       :  number of coincidences, corresponds to row of A,B matrix in batch cell
	- avolt          :  A voltage corresponding to each coincidence
                         :  column number corresponds to each coincidence event in batch matrix
	- bvolt          :  B voltage corresponding to each coincidence
                         :  column number corresponds to each coincidence event in batch matrix
	- time_loc       :  time value of the coincidence (corresponds to which event in the bmatrix)
	- time_diff      :  time difference between the voltage maximums in single coincidence event
                         :  value is scaled based on time difference between sampling.
7.) >> graphCoinc(coinc_result, inputcell)
    - graphcs Coincidence

7.) >> graphs(inputcell,names)
	- Plots graphs in input cell
	- all figures, movie, stack types
	- limitations based on start and end of batch matrix