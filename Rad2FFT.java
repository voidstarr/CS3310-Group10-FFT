import java.io.Console;

/* FFT Radix 2 Implementation
*  Radix 2 FFT implementation without the need for complex numbers using method found in https://www.nayuki.io/res/free-small-fft-in-multiple-languages/Fft.java
*  Input is two arrays, one for the actual array with data we want to use the FFT on (frequencies etc) and the other to hold the corresponding imaginary number
*  that is given by the FFT.
*
*
*/

public class Rad2FFT {
    // Compute the FFT in place using, returning two arrays with the corresponding real and imaginary number for that ith entry. 
    public static void Radix2FFT(double real[], double image[]) {
        int N = real.length;
        int levels = (int)(Math.log(N) / Math.log(2));
        int amountToShift = 32 - levels;

        /* Bit reversed adressing for in place calculation of our array.
        * Bit reversal is actually a fairly easy process. We first reverse i and shift this amount by the appropriate # determined from 32 - levels
        * This works because the variable levels determines the number of stages or steps the FFT will have and consequently this is also the number of
        * bits needed to represent our values 0 - (N-1). By subtracting this value form 32 we get the number to shift our reversed integer by
        * in order to get its correct ordering. i.e. for N = 8, log(2) = 3, meaning we need 3 bits for our reversed address representation and must shit 32 - 3 = 29
        * bits to the right in order to get the reverse of 1 = 10000000000000000000000000000000 to be 4 = 00000000000000000000000000000100
        */
        for(int i = 0; i < N; i++) {
            int j = Integer.reverse(i) >>> amountToShift;
            // For efficiencies sake we dont bother swapping array elements whose binary representation are palindromes
            if(j > i) {
                // If you wanted to be really efficient you could just write the 3 lines of code for swap here to avoid the function call overhead
                swap(real, i, j);
                swap(image, i, j);
            }

        }

        /* Iterative Radix 2 FFT implementation
        *
        *
        */
        for(int i = 1; i * 2 <= N; i *= 2) {
            // Butterfly operation that gives us the DFT between the two pairs l and k
            for(int j = 0; j < N; j += i * 2) {
                for(int k = j; k < j + i; k++) {
                    int l = k + i;
                    double sin = Math.sin(Math.PI * (k-j) / i), 
                           cos = Math.cos(Math.PI * (k-j) / i);
                    double rea = real[l] * cos + image[l] * sin, 
                           img = -real[l] * sin + image[l] * cos;
                    real[l] = real[k] - rea;
                    image[l] = image[k] - img;
                    real[k] += rea;
                    image[k] += img;
                }
            }
        }
        displayFFT(real, image, N);
    }

    private static void swap(double a[], int i, int j) {
        double tmp;
        tmp = a[i];
        a[i] = a[j];
        a[j] = tmp;
    }
    private static void displayFFT(double real[], double image[], int N) {
        System.out.print("FFT: [");
        for(int i = 0; i < N; i++) {
            System.out.print(real[i] + ", ");
        }
        System.out.print("], ");

        System.out.print("[");

        for(int i = 0; i < N; i++) {
            System.out.print(image[i] + ", ");
        }
        System.out.print("]");

    }


    public static void main(String[] args) {
        double[] real = {1.0, 2.0, 3.0, 4.0};
        double[] image = {0.0, 0.0, 0.0, 0.0};
        Rad2FFT.Radix2FFT(real, image);
    }
}