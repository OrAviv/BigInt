import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.lang.ArithmeticException;

//slot 0 will be the sign of the number.
//the number will start at index 1 and as the number growth, the high indexes will have the end of the number.
//for example: number = 87654 - index 0 will have +1 as the sign, and the rest of the indexes as followed:
//1:4, 2:5, 3:6, 4:7 5:8.

public class BigInt implements Comparable
{

    private int sign;
    ArrayList<Integer> number = new ArrayList<Integer>();
    private static final int POSITIVE = 1;
    private static final int NEGATIVE = -1;
    Scanner scanner = new Scanner(System.in);

    public BigInt(String number)
    {
        this.check_number(number);
        if (number.equals(""))
            this.sign = POSITIVE;
        else
            this.set_sign(number.charAt(0));
        this.set_number(number);
    }

    private BigInt()
    {
//        Private constructor doing noting - for inner use.
    }

    public BigInt plus(BigInt addend)
    {
        if (this.get_sign() == NEGATIVE && addend.get_sign() == POSITIVE)
            return addend.private_plus(this);
        if (this.get_sign() == POSITIVE && addend.get_sign() == NEGATIVE)
            return this.private_plus(addend);

        int first;
        int second;
        int sum;
        int carry = 0;
        BigInt int_to_return = new BigInt();

        int max_size = Math.max(this.number.size(), addend.number.size());
        for (int i=0; i<max_size; i++)
        {
            if (i < this.number.size())
                first = this.number.get(i);
            else
                first = 0;
            if (i < addend.number.size())
                second = addend.number.get(i);
            else
                second = 0;
            sum = first + second + carry;
            if (sum > 9)
            {
                int_to_return.number.add(sum%10);
                carry = 1;
            }
            else
            {
                int_to_return.number.add(sum);
                carry = 0;
            }
        }
        if (carry != 0)
            int_to_return.number.add(carry);
        int_to_return.sign = this.get_sign();
        return int_to_return;
    }

    private BigInt private_plus(BigInt addend)
    {
        addend.sign = POSITIVE;
        BigInt int_to_return = this.minus(addend);
        addend.sign = NEGATIVE;
        this.calculate_sign(addend, int_to_return);
        return int_to_return;
    }


    public BigInt minus(BigInt subtract)
    {
        if (this.get_sign() == POSITIVE && subtract.get_sign() == NEGATIVE)
            return this.private_minus(subtract);
        if (this.get_sign() == NEGATIVE && subtract.get_sign() == POSITIVE)
            return subtract.private_minus(this);

        int first;
        int second;
        int sum;
        BigInt int_to_return = new BigInt();

        int max_size = Math.max(this.number.size(), subtract.number.size());
        for (int i=0; i<max_size; i++)
        {
            if (i < this.number.size())
                first = this.number.get(i);
            else
                first = 1;
            if (i < subtract.number.size())
                second = subtract.number.get(i);
            else
                second = 1;
            sum = first - second;
            if (first == 0 && sum < 0)
                sum = sum + 10;
            if (sum < 0)
                sum *= NEGATIVE;
            int_to_return.number.add(sum);
        }
        for (int i=int_to_return.number.size()-1; int_to_return.number.get(i) == 0; i--)
            int_to_return.number.remove(i);
        this.calculate_sign(subtract, int_to_return);
        return int_to_return;
    }


    private BigInt private_minus(BigInt minu)
    {
        minu.sign = POSITIVE;
        BigInt int_to_return = this.plus(minu);
        minu.sign = NEGATIVE;
        this.calculate_sign(minu, int_to_return);
        return int_to_return;
    }

    public BigInt multiply(BigInt multi)
    {
        BigInt int_to_return = new BigInt();
        int first;
        int second;
        int sum;
        int carry = 0;

        for (int i=0; i<this.number.size(); i++)
        {
            first = this.number.get(i);
            for (int j=0; j<multi.number.size(); j++)
            {
                second = multi.number.get(j);
                sum = (first * second) + carry;
                if (sum > 9)
                {
                    carry = sum/10;
                    sum = sum%10;
                }
                else
                    carry = 0;
                if (j+i >= int_to_return.number.size())
                {
                    int_to_return.number.add(j+i, sum);
                    if (j == multi.number.size()-1 && carry != 0)
                        int_to_return.number.add(carry);
                }
                else
                {
                    sum += int_to_return.number.get(j+i);
                    if (sum>9)
                    {
                        carry += sum/10;
                        sum =sum%10;
                    }
                    int_to_return.number.set(j+i, sum);
                }
            }
        }
        int_to_return.sign = this.get_sign() * multi.get_sign();
        return int_to_return;
    }

    public BigInt divide(BigInt divider)
    {
        // didnt have enough time to create. sorry
        return null;
    }

    private void set_sign(char potential_sign)
    {
        if (potential_sign == '-')
        {
            this.sign = NEGATIVE;
        }
        else
            this.sign = POSITIVE;
    }

    private void calculate_sign(BigInt number, BigInt int_to_return)
    {
        if (this.number.size() > number.number.size())
        {
            int_to_return.sign = this.get_sign();
            return;
        }
        if (this.number.size() < number.number.size())
        {
            int_to_return.sign = number.get_sign();
            return;
        }

        if (this.number.get(this.number.size()-1) - number.number.get(number.number.size()-1) >= 0)
            int_to_return.sign = POSITIVE;
        else
            int_to_return.sign = NEGATIVE;
    }

    public int get_sign()
    {
        return this.sign;
    }

    private void check_number(String number)
    {
        for (int i=0; i<number.length(); i++)
        {
            // first index may be a sign.
            if (number.charAt(0) == '-')
                continue;
            if (number.charAt(i) > '9' || number.charAt(i) < '0')
            {
                System.out.println("you number contains non-number chars, please provide another number containing only numbers ");
                number = scanner.nextLine();
                i = 0;
            }
        }
    }

    private void set_number(String number)
    {
        for (int i=number.length()-1; i>=0; i--)
        {
            if (number.charAt(i) == '-')
                continue;
            this.number.add((int)number.charAt(i) - '0');
        }
    }

    @Override
    public int compareTo(Object o)
    {
        BigInt result = this.minus((BigInt)o);
        if (result.sign == NEGATIVE)
            return -1;
        if (this.equals(o))
            return 0;
        return 1;
    }

    @Override
    public String toString()
    {
        String str = "";
        if (this.get_sign() == NEGATIVE)
            str = "-";
        for (int i=this.number.size()-1; i>=0; i--)
            str += String.format("%d",this.number.get(i));
        return str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BigInt)) return false;
        BigInt big_int = (BigInt) o;
        for (int i=0; i<big_int.number.size(); i++)
        {
            if (big_int.number.get(i) != 0)
                return false;
        }
        return true;
    }
}
