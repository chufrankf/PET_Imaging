function bmatrix = bloada(dirname)
%Generates matrix with all the files inside
%   bmatrix  :  matrix(#filesindir, lengthoffile)
%   dirname  :  string name of directory

filenames = what(dirname);
cd(dirname);

% initialize bmatrix for speed
ftemp = load(char(filenames.mat(1)));
bmatrix = zeros(length(filenames.mat), ftemp.Length);

%declare Length, Size
fprintf('Length of Event: %d \n', ftemp.Length);
fprintf('Number of Events: %d \n', length(filenames.mat));

% creating bmatrix
for i = 1:length(filenames.mat)
    file = load(char(filenames.mat(i)));
    bmatrix(i,:) = file.A;
end

cd ..

