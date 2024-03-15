'''
    Author: Dominic MacIsaac
    Feel free to add more tests to this file to ensure your code is working properly.
    To run
        - place this file in the same directory as the java file
        - In terminal, move to the directory containing the java file and this file
        - Run the command: python3 prog4_student_test.py
'''

import subprocess
import re

def write_array_to_file(array, filename):
    """
    Writes each item of an array to a new line in a file named filename.
    Inputs:
        array: List of items to write to the file.
        filename: Name of the file to write to.
    Outputs:
        None
    """
    with open(filename, 'w') as file:
        for item in array:
            file.write(str(item) + '\n')


def compile_java_code_indir(java_file_name):
    """Compiles the Java code and returns True if successful, False otherwise."""
    try:
        subprocess.check_output(["javac", java_file_name], stderr=subprocess.STDOUT)
        return True
    except subprocess.CalledProcessError:
        return False

def run_command(command_list):
    timeout = 10
    try:
        # Run the command and wait for it to complete
        output = subprocess.check_output(command_list, timeout=timeout, stderr=subprocess.STDOUT)
        return output.decode('utf-8').strip()
    except subprocess.TimeoutExpired:
        # Return a message if the process times out
        print("TIMEOUT")
        return f"Function timed out after {timeout} seconds"
    except subprocess.CalledProcessError as e:
        # Return the error output if the command fails
        return e.output.decode('utf-8').strip()
    except Exception as e:
        # General exception handling
        return str(e)

def read_numbers_from_file(file_name: str) ->[int]:
    """
    Read numbers from a file where each number is on a new line and return a list of numbers.
    
    Args:
    file_name (str): The name of the file to read from.
    
    Returns:
    List[int]: A list of numbers read from the file.
    """
    numbers = []
    with open(file_name, 'r') as file:
        for line in file:
            # Attempt to convert each line into an integer and append to the list
            try:
                number = int(line.strip())
                numbers.append(number)
            except ValueError:
                # Skip lines that cannot be converted to an integer
                continue
    
    return numbers

def write_to_file(r_string, file_name):
    """
    Write a raw string to a file.

    :param r_string: A raw string to be written to the file.
    :param file_name: The name of the file where the string will be written.
    """
    with open(file_name, 'w') as file:
        file.write(r_string)  

def parse_output(input_string):
    # Regular expression patterns
    node_pattern = re.compile(r'\[\s*(\d+)\s*\]', re.IGNORECASE)
    unreachable_pattern = re.compile(r'unreachable', re.IGNORECASE)
    distance_pattern = re.compile(r'distance\s*:?\s*(\d+)', re.IGNORECASE)
    path_pattern = re.compile(r'path\s*:?[\s*\(]+([\d,\s]+)[\s*\)]+', re.IGNORECASE)

    result = {}

    # Process each line separately
    for line in input_string.split('\n'):
        if line.strip():  # Skip empty lines
            # Extract node number
            try:
                node = int(node_pattern.search(line).group(1))
                # Check if the node is unreachable
                if unreachable_pattern.search(line):
                    result[node] = {'reachable': False, 'Shortest Path': None, 'Shortest Distance': None}
                else:
                    # Extract distance and path
                    distance = int(distance_pattern.search(line).group(1))
                        
                    path = [int(x) for x in path_pattern.search(line).group(1).split(',')]
                    result[node] = {'reachable': True, 'Shortest Path': path, 'Shortest Distance': distance}
            except:
                pass
    return result


def extract_numbers(input_string):
    """
    Extract numbers from a string that are separated by commas, spaces, or both.

    :param input_string: A string containing numbers separated by commas and/or spaces.
    :return: A list of numbers extracted from the string.
    """
    # Using regular expression to find all numbers in the string
    try:
        numbers = re.findall(r'[\d.]+', input_string)
        
        # Converting each found string to an integer or a float
        return [int(num) if num.isdigit() else float(num) for num in numbers]
    except:
        return input_string
    
def check_ans(expected_list, actual):
    for expected in expected_list:
        if expected == actual:
            return True
        
    print("FAIL: output ", end='')
    print(actual, end=' ')
    print(expected_list[0], end='')
    print(' expected')
    return False
    
test_1 = r'''0:1,2;2,5;3,4
1:3,1
2:
3:2,2'''
test_1_src = '3'
test_1_sol = {0:{'reachable': False, 'Shortest Distance':None, 'Shortest Path':None},
              1:{'reachable': False, 'Shortest Distance':None, 'Shortest Path':None},
              2:{'reachable': True, 'Shortest Distance':2, 'Shortest Path':[[3,2]]}}
test_2_src = '0'
test_2_sol = {1:{'reachable': True, 'Shortest Distance':2, 'Shortest Path':[[0,1]]},
              2:{'reachable': True, 'Shortest Distance':5, 'Shortest Path':[[0,2],[0,1,3,2]]},
              3:{'reachable': True, 'Shortest Distance':3, 'Shortest Path':[[0,1,3]]}}

if __name__ == '__main__':
    # Compile the java code
    print("Compile: ", str(compile_java_code_indir('Dijkstra.java')))

    test_file_name = 'dag_test.txt'
    write_to_file(test_1, test_file_name)
    output = run_command(['java','Dijkstra',test_file_name, test_1_src])
    print(f'Your output:\n{output}')
    output = parse_output(output)
    passed =True
    for key in test_1_sol:
        if key not in list(output.keys()): #check they both exists
            print("key not found")
            passed = False
        elif test_1_sol[key]['reachable'] != output[key]['reachable']: # check they have same reachbility
            print("incorrect reachability")
            passed = False
        elif test_1_sol[key]['reachable']:
            if not check_ans(test_1_sol[key]['Shortest Path'],output[key]['Shortest Path']):
                print("Incorrect Path")
                passed = False
            elif not test_1_sol[key]['Shortest Distance'] == output[key]['Shortest Distance']: 
                print("incorrect distance")
                passed = False
    if passed:
        print("Test 1 Passed")
    else:
        print("Test 1 Failed")
    
    output = run_command(['java','Dijkstra',test_file_name, test_2_src])
    print(f'Your output:\n{output}')
    output = parse_output(output)
    passed =True
    for key in test_2_sol:
        if key not in list(output.keys()): #check they both exists
            print("key not found")
            passed = False
        elif test_2_sol[key]['reachable'] != output[key]['reachable']: # check they have same reachbility
            print("incorrect reachability")
            passed = False
        elif test_2_sol[key]['reachable']:
            if not check_ans(test_2_sol[key]['Shortest Path'],output[key]['Shortest Path']):
                print("Incorrect Path")
                passed = False
            elif not test_2_sol[key]['Shortest Distance'] == output[key]['Shortest Distance']: 
                print("incorrect distance")
                passed = False
    if passed:
        print("Test 2 Passed")
    else:
        print("Test 2 Failed")
